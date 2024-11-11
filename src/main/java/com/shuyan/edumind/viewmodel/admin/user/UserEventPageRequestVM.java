package com.shuyan.edumind.viewmodel.admin.user;


import com.shuyan.edumind.base.BasePage;
import lombok.Data;

@Data
public class UserEventPageRequestVM extends BasePage {

    private Integer userId;

    private String userName;

}
