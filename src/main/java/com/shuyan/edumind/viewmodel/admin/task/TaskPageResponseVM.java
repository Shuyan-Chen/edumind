package com.shuyan.edumind.viewmodel.admin.task;

import lombok.Data;

@Data
public class TaskPageResponseVM {

    private Integer id;

    private String title;

    private Integer gradeLevel;

    private String createUserName;

    private String createTime;

    private Boolean deleted;
}
