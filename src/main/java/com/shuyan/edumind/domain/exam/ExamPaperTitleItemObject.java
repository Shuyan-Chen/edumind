package com.shuyan.edumind.domain.exam;


import lombok.Data;

import java.util.List;

@Data
public class ExamPaperTitleItemObject {

    private String name;

    private List<ExamPaperQuestionItemObject> questionItems;

}
