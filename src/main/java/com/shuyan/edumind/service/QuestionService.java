package com.shuyan.edumind.service;

import com.shuyan.edumind.domain.Question;
import com.shuyan.edumind.viewmodel.admin.question.QuestionEditRequestVM;
import com.shuyan.edumind.viewmodel.admin.question.QuestionPageRequestVM;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuestionService{

    Question findById(Integer id);

    Page<Question> page(QuestionPageRequestVM requestVM);

    Question insertFullQuestion(QuestionEditRequestVM model, Integer userId);

    Question updateFullQuestion(QuestionEditRequestVM model);

    QuestionEditRequestVM getQuestionEditRequestVM(Integer questionId);

    QuestionEditRequestVM getQuestionEditRequestVM(Question question);

    Integer selectAllCount();

    List<Integer> selectMonthCount();
}
