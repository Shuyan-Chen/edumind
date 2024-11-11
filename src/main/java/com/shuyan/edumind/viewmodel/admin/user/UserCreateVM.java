package com.shuyan.edumind.viewmodel.admin.user;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateVM {

    private Integer id;

    @NotBlank
    private String userName;

    private String password;

    @NotBlank
    private String realName;

    private String age;

    private Integer status;

    private Integer sex;

    private String birthDay;

    private String phone;

    private Integer role;

    private Integer userLevel;

}
