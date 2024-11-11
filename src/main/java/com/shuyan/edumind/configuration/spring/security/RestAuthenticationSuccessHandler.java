package com.shuyan.edumind.configuration.spring.security;

import com.shuyan.edumind.base.SystemCode;
import com.shuyan.edumind.domain.UserEventLog;
import com.shuyan.edumind.event.UserEvent;
import com.shuyan.edumind.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;

    @Autowired
    public RestAuthenticationSuccessHandler(ApplicationEventPublisher eventPublisher, UserService userService) {
        this.eventPublisher = eventPublisher;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object object = authentication.getPrincipal();
        if (null != object) {
            User springUser = (User) object;
            com.shuyan.edumind.domain.User user = userService.findByUsername(springUser.getUsername());
            if (null != user) {
                UserEventLog userEventLog = new UserEventLog(user.getId(), user.getUserName(), user.getRealName(), new Date());
                userEventLog.setContent(user.getUserName() + " 登录了学之思开源考试系统");
                eventPublisher.publishEvent(new UserEvent(userEventLog));
                com.shuyan.edumind.domain.User newUser = new com.shuyan.edumind.domain.User();
                newUser.setUserName(user.getUserName());
                newUser.setImagePath(user.getImagePath());
                RestUtil.response(response, SystemCode.OK.getCode(), SystemCode.OK.getMessage(), newUser);
            }
        } else {
            RestUtil.response(response, SystemCode.UNAUTHORIZED.getCode(), SystemCode.UNAUTHORIZED.getMessage());
        }
    }
}
