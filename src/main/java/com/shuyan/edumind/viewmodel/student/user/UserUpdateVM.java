package com.shuyan.edumind.viewmodel.student.user;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateVM {

    @NotBlank
    private String realName;

    private String age;

    private Integer sex;

    private String birthDay;

    private String phone;

    @NotNull
    private Integer userLevel;

}
