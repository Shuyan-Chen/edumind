package com.mindskip.xzs.service.impl;

import com.mindskip.xzs.domain.ExamPaper;
import com.mindskip.xzs.domain.ExamPaperAnswer;
import com.mindskip.xzs.domain.TaskExamCustomerAnswer;
import com.mindskip.xzs.domain.TextContent;
import com.mindskip.xzs.domain.task.TaskItemAnswerObject;
import com.mindskip.xzs.repository.TaskExamCustomerAnswerMapper;
import com.mindskip.xzs.service.TaskExamCustomerAnswerService;
import com.mindskip.xzs.service.TextContentService;
import com.mindskip.xzs.utility.JsonUtil;
import com.shuyan.edumind.domain.ExamPaper;
import com.shuyan.edumind.domain.ExamPaperAnswer;
import com.shuyan.edumind.domain.TaskExamCustomerAnswer;
import com.shuyan.edumind.domain.TextContent;
import com.shuyan.edumind.domain.task.TaskItemAnswerObject;
import com.shuyan.edumind.repository.TaskExamCustomerAnswerRepository;
import com.shuyan.edumind.service.TaskExamCustomerAnswerService;
import com.shuyan.edumind.service.TextContentService;
import com.shuyan.edumind.utility.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TaskExamCustomerAnswerImpl implements TaskExamCustomerAnswerService {

    private final TaskExamCustomerAnswerRepository taskExamCustomerAnswerRepository;
    private final TextContentService textContentService;

    @Autowired
    public TaskExamCustomerAnswerImpl(TaskExamCustomerAnswerRepository taskExamCustomerAnswerRepository, TextContentService textContentService) {
        this.taskExamCustomerAnswerRepository = taskExamCustomerAnswerRepository;
        this.textContentService = textContentService;
    }

    @Override
    public void insertOrUpdate(ExamPaper examPaper, ExamPaperAnswer examPaperAnswer, Date now) {
        Integer taskId = examPaper.getTaskExamId();
        Integer userId = examPaperAnswer.getCreateUser();
        TaskExamCustomerAnswer taskExamCustomerAnswer = taskExamCustomerAnswerRepository.findByTUid(taskId, userId);
        if (null == taskExamCustomerAnswer) {
            taskExamCustomerAnswer = new TaskExamCustomerAnswer();
            taskExamCustomerAnswer.setCreateTime(now);
            taskExamCustomerAnswer.setCreateUser(userId);
            taskExamCustomerAnswer.setTaskExamId(taskId);
            List<TaskItemAnswerObject> taskItemAnswerObjects = Arrays.asList(new TaskItemAnswerObject(examPaperAnswer.getExamPaperId(), examPaperAnswer.getId(), examPaperAnswer.getStatus()));
            TextContent textContent = textContentService.jsonConvertInsert(taskItemAnswerObjects, now, null);
            textContentService.insertByFilter(textContent);
            taskExamCustomerAnswer.setTextContentId(textContent.getId());
            insertByFilter(taskExamCustomerAnswer);
        } else {
            TextContent textContent = textContentService.findById(taskExamCustomerAnswer.getTextContentId());
            List<TaskItemAnswerObject> taskItemAnswerObjects = JsonUtil.toJsonListObject(textContent.getContent(), TaskItemAnswerObject.class);
            taskItemAnswerObjects.add(new TaskItemAnswerObject(examPaperAnswer.getExamPaperId(), examPaperAnswer.getId(), examPaperAnswer.getStatus()));
            textContentService.jsonConvertUpdate(textContent, taskItemAnswerObjects, null);
            textContentService.updateByIdFilter(textContent);
        }
    }

    @Override
    public TaskExamCustomerAnswer selectByTUid(Integer tid, Integer uid) {
        return taskExamCustomerAnswerRepository.findByTUid(tid, uid);
    }

    @Override
    public List<TaskExamCustomerAnswer> selectByTUid(List<Integer> taskIds, Integer uid) {
        return taskExamCustomerAnswerRepository.findByTUid(taskIds, uid);
    }
}
