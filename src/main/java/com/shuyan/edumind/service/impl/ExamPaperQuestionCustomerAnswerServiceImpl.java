package com.shuyan.edumind.service.impl;

import com.shuyan.edumind.domain.ExamPaperQuestionCustomerAnswer;
import com.shuyan.edumind.domain.TextContent;
import com.shuyan.edumind.domain.enums.QuestionTypeEnum;
import com.shuyan.edumind.domain.other.ExamPaperAnswerUpdate;
import com.shuyan.edumind.domain.other.KeyValue;
import com.shuyan.edumind.repository.ExamPaperQuestionCustomerAnswerRepository;
import com.shuyan.edumind.service.ExamPaperQuestionCustomerAnswerService;
import com.shuyan.edumind.service.TextContentService;
import com.shuyan.edumind.utility.DateTimeUtil;
import com.shuyan.edumind.utility.ExamUtil;
import com.shuyan.edumind.utility.JsonUtil;
import com.shuyan.edumind.viewmodel.student.exam.ExamPaperSubmitItemVM;
import com.shuyan.edumind.viewmodel.student.question.answer.QuestionPageStudentRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamPaperQuestionCustomerAnswerServiceImpl implements ExamPaperQuestionCustomerAnswerService {

    private final ExamPaperQuestionCustomerAnswerRepository examPaperQuestionCustomerAnswerRepository;
    private final TextContentService textContentService;

    @Autowired
    public ExamPaperQuestionCustomerAnswerServiceImpl(ExamPaperQuestionCustomerAnswerRepository examPaperQuestionCustomerAnswerRepository, TextContentService textContentService) {
        this.examPaperQuestionCustomerAnswerRepository = examPaperQuestionCustomerAnswerRepository;
        this.textContentService = textContentService;
    }

    @Override
    public ExamPaperQuestionCustomerAnswer findById(Integer id) {
        return examPaperQuestionCustomerAnswerRepository.findById(id).get();
    }

    @Override
    public Page<ExamPaperQuestionCustomerAnswer> studentPage(QuestionPageStudentRequestVM requestVM, Pageable pageable) {
        return examPaperQuestionCustomerAnswerRepository.findByCreateUser(requestVM.getCreateUser(), pageable);
    }

    @Override
    public List<ExamPaperQuestionCustomerAnswer> selectListByPaperAnswerId(Integer id) {
        return examPaperQuestionCustomerAnswerRepository.findByExamPaperAnswerId(id);
    }

    @Override
    public void insertList(List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers) {
        examPaperQuestionCustomerAnswerRepository.saveAll(examPaperQuestionCustomerAnswers);
    }

    @Override
    public ExamPaperSubmitItemVM examPaperQuestionCustomerAnswerToVM(ExamPaperQuestionCustomerAnswer qa) {
        ExamPaperSubmitItemVM examPaperSubmitItemVM = new ExamPaperSubmitItemVM();
        examPaperSubmitItemVM.setId(qa.getId());
        examPaperSubmitItemVM.setQuestionId(qa.getQuestionId());
        examPaperSubmitItemVM.setDoRight(qa.getDoRight());
        examPaperSubmitItemVM.setItemOrder(qa.getItemOrder());
        examPaperSubmitItemVM.setQuestionScore(ExamUtil.scoreToVM(qa.getQuestionScore()));
        examPaperSubmitItemVM.setScore(ExamUtil.scoreToVM(qa.getCustomerScore()));
        setSpecialToVM(examPaperSubmitItemVM, qa);
        return examPaperSubmitItemVM;
    }

    @Override
    public Integer selectAllCount() {
        return (int) examPaperQuestionCustomerAnswerRepository.count();
    }

    @Override
    public List<Integer> findMonthCount() {
        Date startTime = DateTimeUtil.getMonthStartDay();
        Date endTime = DateTimeUtil.getMonthEndDay();
        List<KeyValue> monthCount = examPaperQuestionCustomerAnswerRepository.countByCreateTimeBetween(startTime, endTime);
        List<String> monthStartToNowFormat = DateTimeUtil.MonthStartToNowFormat();
        return monthStartToNowFormat.stream().map(md -> {
            KeyValue keyValue = monthCount.stream().filter(kv -> kv.getName().equals(md)).findAny().orElse(null);
            return null == keyValue ? 0 : keyValue.getValue();
        }).collect(Collectors.toList());
    }

    @Override
    public int updateScore(List<ExamPaperAnswerUpdate> examPaperAnswerUpdates) {
        int totalUpdated = 0;
        for (ExamPaperAnswerUpdate answer : examPaperAnswerUpdates) {
            totalUpdated += examPaperQuestionCustomerAnswerRepository.updateScores(
                    answer.getId(),
                    answer.getCustomerScore(),
                    answer.getDoRight()
            );
        }
        return totalUpdated;
    }

    private void setSpecialToVM(ExamPaperSubmitItemVM examPaperSubmitItemVM, ExamPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(examPaperQuestionCustomerAnswer.getQuestionType());
        switch (questionTypeEnum) {
            case MultipleChoice:
                examPaperSubmitItemVM.setContent(examPaperQuestionCustomerAnswer.getAnswer());
                examPaperSubmitItemVM.setContentArray(ExamUtil.contentToArray(examPaperQuestionCustomerAnswer.getAnswer()));
                break;
            case GapFilling:
                TextContent textContent = textContentService.findById(examPaperQuestionCustomerAnswer.getTextContentId()).get();
                List<String> correctAnswer = JsonUtil.toJsonListObject(textContent.getContent(), String.class);
                examPaperSubmitItemVM.setContentArray(correctAnswer);
                break;
            default:
                if (QuestionTypeEnum.needSaveTextContent(examPaperQuestionCustomerAnswer.getQuestionType())) {
                    TextContent content = textContentService.findById(examPaperQuestionCustomerAnswer.getTextContentId()).get();
                    examPaperSubmitItemVM.setContent(content.getContent());
                } else {
                    examPaperSubmitItemVM.setContent(examPaperQuestionCustomerAnswer.getAnswer());
                }
                break;
        }
    }
}
