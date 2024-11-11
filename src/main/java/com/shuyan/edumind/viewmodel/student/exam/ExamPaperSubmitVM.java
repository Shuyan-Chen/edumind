package com.shuyan.edumind.viewmodel.student.exam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ExamPaperSubmitVM {

    @NotNull
    private Integer id;

    @NotNull
    private Integer doTime;

    private String score;

    @NotNull
    @Valid
    private List<ExamPaperSubmitItemVM> answerItems;

}
