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
@Table(name = "t_exam_paper")
public class ExamPaper implements Serializable {

    private static final long serialVersionUID = 6522325562798936825L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer subjectId;

    private Integer paperType;

    private Integer gradeLevel;

    private Integer score;

    private Integer questionCount;

    private Integer suggestTime;

    private Date limitStartTime;

    private Date limitEndTime;

    private Integer frameTextContentId;

    private Integer createUser;

    private Date createTime;

    private Boolean deleted;

    private Integer taskExamId;

}
