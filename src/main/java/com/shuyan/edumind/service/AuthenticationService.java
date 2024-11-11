package com.shuyan.edumind.service;

import com.shuyan.edumind.domain.User;

public interface AuthenticationService {

    boolean authUser(String username, String password);

    boolean authUser(User user, String username, String password);

    String pwdEncode(String password);

    String pwdDecode(String encodePwd);
}
