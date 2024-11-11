package com.shuyan.edumind.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_exam_paper_answer")
public class ExamPaperAnswer{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer examPaperId;

    private String paperName;

    private Integer paperType;

    private Integer subjectId;

    private Integer systemScore;

    private Integer userScore;

    private Integer paperScore;

    private Integer questionCorrect;

    private Integer questionCount;

    private Integer doTime;

    private Integer status;

    private Integer createUser;

    private Date createTime;

    private Integer taskExamId;

}
