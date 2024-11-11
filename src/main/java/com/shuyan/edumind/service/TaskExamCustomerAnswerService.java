package com.shuyan.edumind.service;

import com.shuyan.edumind.domain.ExamPaper;
import com.shuyan.edumind.domain.ExamPaperAnswer;
import com.shuyan.edumind.domain.TaskExamCustomerAnswer;

import java.util.Date;
import java.util.List;

public interface TaskExamCustomerAnswerService {

    void insertOrUpdate(ExamPaper examPaper, ExamPaperAnswer examPaperAnswer, Date now);

    TaskExamCustomerAnswer findByTUid(Integer tid, Integer uid);

    List<TaskExamCustomerAnswer> selectByTUid(List<Integer> taskIds, Integer uid);
}
