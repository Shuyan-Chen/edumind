package com.shuyan.edumind.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ExamPaperAnswerInfo implements Serializable {

    private static final long serialVersionUID = -4623975914149547563L;
    public ExamPaper examPaper;
    public ExamPaperAnswer examPaperAnswer;
    public List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers;

}
