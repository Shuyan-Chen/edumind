package com.shuyan.edumind.service;


import com.shuyan.edumind.domain.ExamPaper;
import com.shuyan.edumind.domain.User;
import com.shuyan.edumind.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.shuyan.edumind.viewmodel.admin.exam.ExamPaperPageRequestVM;
import com.shuyan.edumind.viewmodel.student.dashboard.PaperFilter;
import com.shuyan.edumind.viewmodel.student.dashboard.PaperInfo;
import com.shuyan.edumind.viewmodel.student.exam.ExamPaperPageVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExamPaperService {

    Page<ExamPaper> page(ExamPaperPageRequestVM requestVM);

    Page<ExamPaper> taskExamPage(ExamPaperPageRequestVM requestVM);

    Page<ExamPaper> studentPage(ExamPaperPageVM requestVM, Pageable pageable);

    ExamPaper savePaperFromVM(ExamPaperEditRequestVM examPaperEditRequestVM, User user);

    ExamPaperEditRequestVM examPaperToVM(Integer id);

    List<PaperInfo> indexPaper(PaperFilter paperFilter);

    Integer selectAllCount();

    List<Integer> selectMonthCount();
}
