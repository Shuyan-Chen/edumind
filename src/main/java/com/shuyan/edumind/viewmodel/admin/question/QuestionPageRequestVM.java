package com.shuyan.edumind.viewmodel.admin.question;


import com.shuyan.edumind.base.BasePage;
import lombok.Data;

@Data
public class QuestionPageRequestVM extends BasePage {

    private Integer id;
    private Integer level;
    private Integer subjectId;
    private Integer questionType;
    private String content;

}
