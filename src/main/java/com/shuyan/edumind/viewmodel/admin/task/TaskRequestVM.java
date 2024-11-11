package com.shuyan.edumind.viewmodel.admin.task;

import com.shuyan.edumind.viewmodel.admin.exam.ExamResponseVM;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class TaskRequestVM {
    private Integer id;

    @NotNull
    private Integer gradeLevel;

    @NotNull
    private String title;

    @Size(min = 1, message = "请添加试卷")
    @Valid
    private List<ExamResponseVM> paperItems;
}
