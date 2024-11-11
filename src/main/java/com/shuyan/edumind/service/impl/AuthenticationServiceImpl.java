package com.shuyan.edumind.service.impl;

import com.shuyan.edumind.configuration.property.SystemConfig;
import com.shuyan.edumind.domain.User;
import com.shuyan.edumind.service.AuthenticationService;
import com.shuyan.edumind.service.UserService;
import com.shuyan.edumind.utility.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final SystemConfig systemConfig;

    @Autowired
    public AuthenticationServiceImpl(UserService userService, SystemConfig systemConfig) {
        this.userService = userService;
        this.systemConfig = systemConfig;
    }

    @Override
    public boolean authUser(String username, String password) {
        User user = userService.findByUsername(username);
        return authUser(user, username, password);
    }

    @Override
    public boolean authUser(User user, String username, String password) {
        if (user == null) {
            return false;
        }
        String encodePwd = user.getPassword();
        if (null == encodePwd || encodePwd.length() == 0) {
            return false;
        }
        String pwd = pwdDecode(encodePwd);
        return pwd.equals(password);
    }

    @Override
    public String pwdEncode(String password) {
        return RsaUtil.rsaEncode(systemConfig.getPwdKey().getPublicKey(), password);
    }

    @Override
    public String pwdDecode(String encodePwd) {
        return RsaUtil.rsaDecode(systemConfig.getPwdKey().getPrivateKey(), encodePwd);
    }


}
