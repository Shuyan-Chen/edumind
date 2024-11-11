package com.shuyan.edumind.domain.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskItemAnswerObject {
    private Integer examPaperId;
    private Integer examPaperAnswerId;
    private Integer status;
}
