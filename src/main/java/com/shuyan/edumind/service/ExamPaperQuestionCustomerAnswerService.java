package com.shuyan.edumind.service;


import com.shuyan.edumind.domain.ExamPaperQuestionCustomerAnswer;
import com.shuyan.edumind.domain.other.ExamPaperAnswerUpdate;
import com.shuyan.edumind.viewmodel.student.exam.ExamPaperSubmitItemVM;
import com.shuyan.edumind.viewmodel.student.question.answer.QuestionPageStudentRequestVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExamPaperQuestionCustomerAnswerService {

    ExamPaperQuestionCustomerAnswer findById(Integer id);

    Page<ExamPaperQuestionCustomerAnswer> studentPage(QuestionPageStudentRequestVM requestVM, Pageable pageable);

    List<ExamPaperQuestionCustomerAnswer> selectListByPaperAnswerId(Integer id);

    ExamPaperSubmitItemVM examPaperQuestionCustomerAnswerToVM(ExamPaperQuestionCustomerAnswer qa);

    Integer selectAllCount();

    List<Integer> findMonthCount();

    int updateScore(List<ExamPaperAnswerUpdate> examPaperAnswerUpdates);
}
