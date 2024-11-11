package com.shuyan.edumind.viewmodel.student.exam;

import com.shuyan.edumind.base.BasePage;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExamPaperPageVM extends BasePage {
    @NotNull
    private Integer paperType;
    private Integer subjectId;
    private Integer levelId;

}
