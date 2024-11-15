package com.shuyan.edumind.viewmodel.admin.question;


import com.shuyan.edumind.viewmodel.BaseVM;
import lombok.Data;

@Data
public class QuestionResponseVM extends BaseVM {

    private Integer id;

    private Integer questionType;

    private Integer textContentId;

    private String createTime;

    private Integer subjectId;

    private Integer createUser;

    private String score;

    private Integer status;

    private String correct;

    private Integer analyzeTextContentId;

    private Integer difficult;

    private String shortTitle;

}
