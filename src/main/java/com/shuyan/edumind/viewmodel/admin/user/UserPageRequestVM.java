package com.shuyan.edumind.viewmodel.admin.user;

import com.shuyan.edumind.base.BasePage;
import lombok.Data;

@Data
public class UserPageRequestVM extends BasePage {

    private String userName;
    private Integer role;

}
