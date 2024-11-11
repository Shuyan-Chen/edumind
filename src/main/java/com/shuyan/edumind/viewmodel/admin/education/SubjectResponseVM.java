package com.shuyan.edumind.viewmodel.admin.education;

import com.shuyan.edumind.viewmodel.BaseVM;
import lombok.Data;

@Data
public class SubjectResponseVM extends BaseVM {
    private Integer id;

    private String name;

    private Integer level;

    private String levelName;

}
