package com.shuyan.edumind.viewmodel.student.education;


import com.shuyan.edumind.viewmodel.BaseVM;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubjectEditRequestVM extends BaseVM {

    private Integer id;

    @NotBlank
    private String name;

    @NotNull
    private Integer level;

    @NotBlank
    private String levelName;


}
