package com.shuyan.edumind.viewmodel.admin.question;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuestionEditItemVM {
    @NotBlank
    private String prefix;
    @NotBlank
    private String content;

    private String score;

    private String itemUuid;

}
