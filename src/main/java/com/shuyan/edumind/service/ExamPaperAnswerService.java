package com.shuyan.edumind.service;


import com.shuyan.edumind.domain.ExamPaperAnswer;
import com.shuyan.edumind.domain.ExamPaperAnswerInfo;
import com.shuyan.edumind.domain.User;
import com.shuyan.edumind.viewmodel.admin.paper.ExamPaperAnswerPageRequestVM;
import com.shuyan.edumind.viewmodel.student.exam.ExamPaperSubmitVM;
import com.shuyan.edumind.viewmodel.student.exampaper.ExamPaperAnswerPageVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExamPaperAnswerService {

    Page<ExamPaperAnswer> studentPage(ExamPaperAnswerPageVM requestVM, Pageable pageable);

    ExamPaperAnswer findById(Integer id);

    ExamPaperAnswerInfo calculateExamPaperAnswer(ExamPaperSubmitVM examPaperSubmitVM, User user);

    String judge(ExamPaperSubmitVM examPaperSubmitVM);

    ExamPaperSubmitVM examPaperAnswerToVM(Integer id);

    Integer selectAllCount();

    List<Integer> selectMonthCount();

    Page<ExamPaperAnswer> adminPage(ExamPaperAnswerPageRequestVM requestVM);
}
