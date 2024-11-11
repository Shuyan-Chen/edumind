package com.shuyan.edumind.viewmodel.admin.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateVM {

    @NotBlank
    private String realName;

    @NotBlank
    private String phone;
}
