package com.shuyan.edumind.service.impl;

import com.shuyan.edumind.domain.ExamPaper;
import com.shuyan.edumind.domain.ExamPaperAnswer;
import com.shuyan.edumind.domain.ExamPaperAnswerInfo;
import com.shuyan.edumind.domain.User;
import com.shuyan.edumind.domain.enums.ExamPaperTypeEnum;
import com.shuyan.edumind.repository.ExamPaperAnswerRepository;
import com.shuyan.edumind.repository.ExamPaperRepository;
import com.shuyan.edumind.repository.QuestionRepository;
import com.shuyan.edumind.repository.TaskExamCustomerAnswerRepository;
import com.shuyan.edumind.service.ExamPaperAnswerService;
import com.shuyan.edumind.service.ExamPaperQuestionCustomerAnswerService;
import com.shuyan.edumind.service.TextContentService;
import com.shuyan.edumind.viewmodel.student.exam.ExamPaperSubmitVM;
import com.shuyan.edumind.viewmodel.student.exampaper.ExamPaperAnswerPageVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamPaperAnswerServiceImpl implements ExamPaperAnswerService {

    private final ExamPaperAnswerRepository examPaperAnswerRepository;
    private final ExamPaperRepository examPaperRepository;
    private final TextContentService textContentService;
    private final QuestionRepository questionRepository;
    private final ExamPaperQuestionCustomerAnswerService examPaperQuestionCustomerAnswerService;
    private final TaskExamCustomerAnswerRepository taskExamCustomerAnswerRepository;

    @Autowired
    public ExamPaperAnswerServiceImpl(ExamPaperAnswerRepository examPaperAnswerRepository, ExamPaperRepository examPaperRepository, TextContentService textContentService, QuestionRepository questionRepository, ExamPaperQuestionCustomerAnswerService examPaperQuestionCustomerAnswerService, TaskExamCustomerAnswerRepository taskExamCustomerAnswerRepository) {
        this.examPaperAnswerRepository = examPaperAnswerRepository;
        this.examPaperRepository = examPaperRepository;
        this.textContentService = textContentService;
        this.questionRepository = questionRepository;
        this.examPaperQuestionCustomerAnswerService = examPaperQuestionCustomerAnswerService;
        this.taskExamCustomerAnswerRepository = taskExamCustomerAnswerRepository;
    }

    @Override
    public Page<ExamPaperAnswer> studentPage(ExamPaperAnswerPageVM requestVM, Pageable pageable) {
        return examPaperAnswerRepository.findBySubjectIdAndCAndCreateUser(requestVM.getSubjectId(),requestVM.getCreateUser(),pageable);
    }

    @Override
    public ExamPaperAnswer findById(Integer id) {
        return examPaperAnswerRepository.findById(id).get();
    }

    @Override
    public ExamPaperAnswerInfo calculateExamPaperAnswer(ExamPaperSubmitVM examPaperSubmitVM, User user) {
        ExamPaperAnswerInfo examPaperAnswerInfo = new ExamPaperAnswerInfo();
        Date now = new Date();
        ExamPaper examPaper = examPaperRepository.findById(examPaperSubmitVM.getId()).get();
        ExamPaperTypeEnum paperTypeEnum = ExamPaperTypeEnum.fromCode(examPaper.getPaperType());
        //任务试卷只能做一次
        if (paperTypeEnum == ExamPaperTypeEnum.Task) {
            ExamPaperAnswer examPaperAnswer = examPaperAnswerRepository.getByPidUid(examPaperSubmitVM.getId(), user.getId());
            if (null != examPaperAnswer)
                return null;
        }
        String frameTextContent = textContentService.selectById(examPaper.getFrameTextContentId()).getContent();
        List<ExamPaperTitleItemObject> examPaperTitleItemObjects = JsonUtil.toJsonListObject(frameTextContent, ExamPaperTitleItemObject.class);
        List<Integer> questionIds = examPaperTitleItemObjects.stream().flatMap(t -> t.getQuestionItems().stream().map(q -> q.getId())).collect(Collectors.toList());
        List<Question> questions = questionMapper.selectByIds(questionIds);
        //将题目结构的转化为题目答案
        List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers = examPaperTitleItemObjects.stream()
                .flatMap(t -> t.getQuestionItems().stream()
                        .map(q -> {
                            Question question = questions.stream().filter(tq -> tq.getId().equals(q.getId())).findFirst().get();
                            ExamPaperSubmitItemVM customerQuestionAnswer = examPaperSubmitVM.getAnswerItems().stream()
                                    .filter(tq -> tq.getQuestionId().equals(q.getId()))
                                    .findFirst()
                                    .orElse(null);
                            return ExamPaperQuestionCustomerAnswerFromVM(question, customerQuestionAnswer, examPaper, q.getItemOrder(), user, now);
                        })
                ).collect(Collectors.toList());

        ExamPaperAnswer examPaperAnswer = ExamPaperAnswerFromVM(examPaperSubmitVM, examPaper, examPaperQuestionCustomerAnswers, user, now);
        examPaperAnswerInfo.setExamPaper(examPaper);
        examPaperAnswerInfo.setExamPaperAnswer(examPaperAnswer);
        examPaperAnswerInfo.setExamPaperQuestionCustomerAnswers(examPaperQuestionCustomerAnswers);
        return examPaperAnswerInfo;
    }

    @Override
    @Transactional
    public String judge(ExamPaperSubmitVM examPaperSubmitVM) {
        ExamPaperAnswer examPaperAnswer = examPaperAnswerMapper.selectByPrimaryKey(examPaperSubmitVM.getId());
        List<ExamPaperSubmitItemVM> judgeItems = examPaperSubmitVM.getAnswerItems().stream().filter(d -> d.getDoRight() == null).collect(Collectors.toList());
        List<ExamPaperAnswerUpdate> examPaperAnswerUpdates = new ArrayList<>(judgeItems.size());
        Integer customerScore = examPaperAnswer.getUserScore();
        Integer questionCorrect = examPaperAnswer.getQuestionCorrect();
        for (ExamPaperSubmitItemVM d : judgeItems) {
            ExamPaperAnswerUpdate examPaperAnswerUpdate = new ExamPaperAnswerUpdate();
            examPaperAnswerUpdate.setId(d.getId());
            examPaperAnswerUpdate.setCustomerScore(ExamUtil.scoreFromVM(d.getScore()));
            boolean doRight = examPaperAnswerUpdate.getCustomerScore().equals(ExamUtil.scoreFromVM(d.getQuestionScore()));
            examPaperAnswerUpdate.setDoRight(doRight);
            examPaperAnswerUpdates.add(examPaperAnswerUpdate);
            customerScore += examPaperAnswerUpdate.getCustomerScore();
            if (examPaperAnswerUpdate.getDoRight()) {
                ++questionCorrect;
            }
        }
        examPaperAnswer.setUserScore(customerScore);
        examPaperAnswer.setQuestionCorrect(questionCorrect);
        examPaperAnswer.setStatus(ExamPaperAnswerStatusEnum.Complete.getCode());
        examPaperAnswerMapper.updateByPrimaryKeySelective(examPaperAnswer);
        examPaperQuestionCustomerAnswerService.updateScore(examPaperAnswerUpdates);

        ExamPaperTypeEnum examPaperTypeEnum = ExamPaperTypeEnum.fromCode(examPaperAnswer.getPaperType());
        switch (examPaperTypeEnum) {
            case Task:
                //任务试卷批改完成后，需要更新任务的状态
                ExamPaper examPaper = examPaperMapper.selectByPrimaryKey(examPaperAnswer.getExamPaperId());
                Integer taskId = examPaper.getTaskExamId();
                Integer userId = examPaperAnswer.getCreateUser();
                TaskExamCustomerAnswer taskExamCustomerAnswer = taskExamCustomerAnswerMapper.getByTUid(taskId, userId);
                TextContent textContent = textContentService.selectById(taskExamCustomerAnswer.getTextContentId());
                List<TaskItemAnswerObject> taskItemAnswerObjects = JsonUtil.toJsonListObject(textContent.getContent(), TaskItemAnswerObject.class);
                taskItemAnswerObjects.stream()
                        .filter(d -> d.getExamPaperAnswerId().equals(examPaperAnswer.getId()))
                        .findFirst().ifPresent(taskItemAnswerObject -> taskItemAnswerObject.setStatus(examPaperAnswer.getStatus()));
                textContentService.jsonConvertUpdate(textContent, taskItemAnswerObjects, null);
                textContentService.updateByIdFilter(textContent);
                break;
            default:
                break;
        }
        return ExamUtil.scoreToVM(customerScore);
    }

    @Override
    public ExamPaperSubmitVM examPaperAnswerToVM(Integer id) {
        ExamPaperSubmitVM examPaperSubmitVM = new ExamPaperSubmitVM();
        ExamPaperAnswer examPaperAnswer = examPaperAnswerMapper.selectByPrimaryKey(id);
        examPaperSubmitVM.setId(examPaperAnswer.getId());
        examPaperSubmitVM.setDoTime(examPaperAnswer.getDoTime());
        examPaperSubmitVM.setScore(ExamUtil.scoreToVM(examPaperAnswer.getUserScore()));
        List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers = examPaperQuestionCustomerAnswerService.selectListByPaperAnswerId(examPaperAnswer.getId());
        List<ExamPaperSubmitItemVM> examPaperSubmitItemVMS = examPaperQuestionCustomerAnswers.stream()
                .map(a -> examPaperQuestionCustomerAnswerService.examPaperQuestionCustomerAnswerToVM(a))
                .collect(Collectors.toList());
        examPaperSubmitVM.setAnswerItems(examPaperSubmitItemVMS);
        return examPaperSubmitVM;
    }

    @Override
    public Integer selectAllCount() {
        return examPaperAnswerMapper.selectAllCount();
    }

    @Override
    public List<Integer> selectMothCount() {
        Date startTime = DateTimeUtil.getMonthStartDay();
        Date endTime = DateTimeUtil.getMonthEndDay();
        List<KeyValue> mouthCount = examPaperAnswerMapper.selectCountByDate(startTime, endTime);
        List<String> mothStartToNowFormat = DateTimeUtil.MothStartToNowFormat();
        return mothStartToNowFormat.stream().map(md -> {
            KeyValue keyValue = mouthCount.stream().filter(kv -> kv.getName().equals(md)).findAny().orElse(null);
            return null == keyValue ? 0 : keyValue.getValue();
        }).collect(Collectors.toList());
    }


    /**
     * 用户提交答案的转化存储对象
     *
     * @param question               question
     * @param customerQuestionAnswer customerQuestionAnswer
     * @param examPaper              examPaper
     * @param itemOrder              itemOrder
     * @param user                   user
     * @param now                    now
     * @return ExamPaperQuestionCustomerAnswer
     */
    private ExamPaperQuestionCustomerAnswer ExamPaperQuestionCustomerAnswerFromVM(Question question, ExamPaperSubmitItemVM customerQuestionAnswer, ExamPaper examPaper, Integer itemOrder, User user, Date now) {
        ExamPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer = new ExamPaperQuestionCustomerAnswer();
        examPaperQuestionCustomerAnswer.setQuestionId(question.getId());
        examPaperQuestionCustomerAnswer.setExamPaperId(examPaper.getId());
        examPaperQuestionCustomerAnswer.setQuestionScore(question.getScore());
        examPaperQuestionCustomerAnswer.setSubjectId(examPaper.getSubjectId());
        examPaperQuestionCustomerAnswer.setItemOrder(itemOrder);
        examPaperQuestionCustomerAnswer.setCreateTime(now);
        examPaperQuestionCustomerAnswer.setCreateUser(user.getId());
        examPaperQuestionCustomerAnswer.setQuestionType(question.getQuestionType());
        examPaperQuestionCustomerAnswer.setQuestionTextContentId(question.getInfoTextContentId());
        if (null == customerQuestionAnswer) {
            examPaperQuestionCustomerAnswer.setCustomerScore(0);
        } else {
            setSpecialFromVM(examPaperQuestionCustomerAnswer, question, customerQuestionAnswer);
        }
        return examPaperQuestionCustomerAnswer;
    }

    /**
     * 判断提交答案是否正确，保留用户提交的答案
     *
     * @param examPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer
     * @param question                        question
     * @param customerQuestionAnswer          customerQuestionAnswer
     */
    private void setSpecialFromVM(ExamPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer, Question question, ExamPaperSubmitItemVM customerQuestionAnswer) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(examPaperQuestionCustomerAnswer.getQuestionType());
        switch (questionTypeEnum) {
            case SingleChoice:
            case TrueFalse:
                examPaperQuestionCustomerAnswer.setAnswer(customerQuestionAnswer.getContent());
                examPaperQuestionCustomerAnswer.setDoRight(question.getCorrect().equals(customerQuestionAnswer.getContent()));
                examPaperQuestionCustomerAnswer.setCustomerScore(examPaperQuestionCustomerAnswer.getDoRight() ? question.getScore() : 0);
                break;
            case MultipleChoice:
                String customerAnswer = ExamUtil.contentToString(customerQuestionAnswer.getContentArray());
                examPaperQuestionCustomerAnswer.setAnswer(customerAnswer);
                examPaperQuestionCustomerAnswer.setDoRight(customerAnswer.equals(question.getCorrect()));
                examPaperQuestionCustomerAnswer.setCustomerScore(examPaperQuestionCustomerAnswer.getDoRight() ? question.getScore() : 0);
                break;
            case GapFilling:
                String correctAnswer = JsonUtil.toJsonStr(customerQuestionAnswer.getContentArray());
                examPaperQuestionCustomerAnswer.setAnswer(correctAnswer);
                examPaperQuestionCustomerAnswer.setCustomerScore(0);
                break;
            default:
                examPaperQuestionCustomerAnswer.setAnswer(customerQuestionAnswer.getContent());
                examPaperQuestionCustomerAnswer.setCustomerScore(0);
                break;
        }
    }

    private ExamPaperAnswer ExamPaperAnswerFromVM(ExamPaperSubmitVM examPaperSubmitVM, ExamPaper examPaper, List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers, User user, Date now) {
        Integer systemScore = examPaperQuestionCustomerAnswers.stream().mapToInt(a -> a.getCustomerScore()).sum();
        long questionCorrect = examPaperQuestionCustomerAnswers.stream().filter(a -> a.getCustomerScore().equals(a.getQuestionScore())).count();
        ExamPaperAnswer examPaperAnswer = new ExamPaperAnswer();
        examPaperAnswer.setPaperName(examPaper.getName());
        examPaperAnswer.setDoTime(examPaperSubmitVM.getDoTime());
        examPaperAnswer.setExamPaperId(examPaper.getId());
        examPaperAnswer.setCreateUser(user.getId());
        examPaperAnswer.setCreateTime(now);
        examPaperAnswer.setSubjectId(examPaper.getSubjectId());
        examPaperAnswer.setQuestionCount(examPaper.getQuestionCount());
        examPaperAnswer.setPaperScore(examPaper.getScore());
        examPaperAnswer.setPaperType(examPaper.getPaperType());
        examPaperAnswer.setSystemScore(systemScore);
        examPaperAnswer.setUserScore(systemScore);
        examPaperAnswer.setTaskExamId(examPaper.getTaskExamId());
        examPaperAnswer.setQuestionCorrect((int) questionCorrect);
        boolean needJudge = examPaperQuestionCustomerAnswers.stream().anyMatch(d -> QuestionTypeEnum.needSaveTextContent(d.getQuestionType()));
        if (needJudge) {
            examPaperAnswer.setStatus(ExamPaperAnswerStatusEnum.WaitJudge.getCode());
        } else {
            examPaperAnswer.setStatus(ExamPaperAnswerStatusEnum.Complete.getCode());
        }
        return examPaperAnswer;
    }


    @Override
    public PageInfo<ExamPaperAnswer> adminPage(com.mindskip.xzs.viewmodel.admin.paper.ExamPaperAnswerPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperAnswerMapper.adminPage(requestVM));
    }
}
