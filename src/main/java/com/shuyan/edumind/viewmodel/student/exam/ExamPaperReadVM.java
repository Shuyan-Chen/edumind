package com.shuyan.edumind.viewmodel.student.exam;

import com.shuyan.edumind.viewmodel.admin.exam.ExamPaperEditRequestVM;
import lombok.Data;

@Data
public class ExamPaperReadVM {
    private ExamPaperEditRequestVM paper;
    private ExamPaperSubmitVM answer;

}
