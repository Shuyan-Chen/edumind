package com.shuyan.edumind.listener;

import com.shuyan.edumind.event.UserEvent;
import com.shuyan.edumind.service.UserEventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserLogListener implements ApplicationListener<UserEvent> {

    private final UserEventLogService userEventLogService;

    /**
     * Instantiates a new User log listener.
     *
     * @param userEventLogService the user event log service
     */
    @Autowired
    public UserLogListener(UserEventLogService userEventLogService) {
        this.userEventLogService = userEventLogService;
    }

    @Override
    public void onApplicationEvent(UserEvent userEvent) {
        userEventLogService.insertByFilter(userEvent.getUserEventLog());
    }

}
