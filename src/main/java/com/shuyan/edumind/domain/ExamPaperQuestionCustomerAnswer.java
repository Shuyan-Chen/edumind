package com.shuyan.edumind.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_exam_paper_question_customer_answer")
public class ExamPaperQuestionCustomerAnswer{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer questionId;

    private Integer examPaperId;

    private Integer examPaperAnswerId;

    private Integer questionType;

    private Integer subjectId;

    private Integer customerScore;

    private Integer questionScore;

    private Integer questionTextContentId;

    private String answer;

    private Integer textContentId;

    private Boolean doRight;

    private Integer createUser;

    private Date createTime;

    private Integer itemOrder;

}
