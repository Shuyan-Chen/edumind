package com.shuyan.edumind.viewmodel.student.question.answer;

import com.shuyan.edumind.viewmodel.admin.question.QuestionEditRequestVM;
import com.shuyan.edumind.viewmodel.student.exam.ExamPaperSubmitItemVM;
import lombok.Data;

@Data
public class QuestionAnswerVM {
    private QuestionEditRequestVM questionVM;
    private ExamPaperSubmitItemVM questionAnswerVM;

}
