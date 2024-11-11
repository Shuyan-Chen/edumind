package com.shuyan.edumind.controller.student;

import com.shuyan.edumind.base.BaseApiController;
import com.shuyan.edumind.base.RestResponse;
import com.shuyan.edumind.domain.Message;
import com.shuyan.edumind.domain.MessageUser;
import com.shuyan.edumind.domain.User;
import com.shuyan.edumind.domain.UserEventLog;
import com.shuyan.edumind.domain.enums.RoleEnum;
import com.shuyan.edumind.domain.enums.UserStatusEnum;
import com.shuyan.edumind.event.UserEvent;
import com.shuyan.edumind.service.AuthenticationService;
import com.shuyan.edumind.service.MessageService;
import com.shuyan.edumind.service.UserEventLogService;
import com.shuyan.edumind.service.UserService;
import com.shuyan.edumind.utility.DateTimeUtil;
import com.shuyan.edumind.viewmodel.student.user.*;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.stream.Collectors;

@RestController("StudentUserController")
@RequestMapping(value = "/api/student/user")
public class UserController extends BaseApiController {

    private final UserService userService;
    private final UserEventLogService userEventLogService;
    private final MessageService messageService;
    private final AuthenticationService authenticationService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserController(UserService userService, UserEventLogService userEventLogService, MessageService messageService, AuthenticationService authenticationService, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.userEventLogService = userEventLogService;
        this.messageService = messageService;
        this.authenticationService = authenticationService;
        this.eventPublisher = eventPublisher;
    }

    @RequestMapping(value = "/current", method = RequestMethod.POST)
    public RestResponse<UserResponseVM> current() {
        User user = getCurrentUser();
        UserResponseVM userVm = UserResponseVM.from(user);
        return RestResponse.ok(userVm);
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RestResponse register(@RequestBody @Valid UserRegisterVM model) {
        User existUser = userService.findByUserName(model.getUserName());
        if (null != existUser) {
            return new RestResponse<>(2, "用户已存在");
        }
        User user = modelMapper.map(model, User.class);
        String encodePwd = authenticationService.pwdEncode(model.getPassword());
        user.setUserUuid(UUID.randomUUID().toString());
        user.setPassword(encodePwd);
        user.setRole(RoleEnum.STUDENT.getCode());
        user.setStatus(UserStatusEnum.Enable.getCode());
        user.setLastActiveTime(new Date());
        user.setCreateTime(new Date());
        user.setDeleted(false);
        userService.save(user);
        UserEventLog userEventLog = new UserEventLog(user.getId(), user.getUserName(), user.getRealName(), new Date());
        userEventLog.setContent("welcome" + user.getUserName()+ "to edumind online exam system");
        eventPublisher.publishEvent(new UserEvent(userEventLog));
        return RestResponse.ok();
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResponse update(@RequestBody @Valid UserUpdateVM model) {
        if (StringUtils.isBlank(model.getBirthDay())) {
            model.setBirthDay(null);
        }
        User user = userService.findById(getCurrentUser().getId());
        modelMapper.map(model, user);
        user.setModifyTime(new Date());
        userService.save(user);
        UserEventLog userEventLog = new UserEventLog(user.getId(), user.getUserName(), user.getRealName(), new Date());
        userEventLog.setContent(user.getUserName() + " updated personal info");
        return RestResponse.ok();
    }

    @RequestMapping(value = "/log", method = RequestMethod.POST)
    public RestResponse<List<UserEventLogVM>> log() {
        User user = getCurrentUser();
        List<UserEventLog> userEventLogs = userEventLogService.findByUserId(user.getId());
        List<UserEventLogVM> userEventLogVMS = userEventLogs.stream().map(d -> {
            UserEventLogVM vm = modelMapper.map(d, UserEventLogVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(d.getCreateTime()));
            return vm;
        }).collect(Collectors.toList());
        return RestResponse.ok(userEventLogVMS);
    }

    @RequestMapping(value = "/message/page", method = RequestMethod.POST)
    public RestResponse<Page<MessageResponseVM>> messagePageList(@RequestBody MessageRequestVM messageRequestVM) {
        messageRequestVM.setReceiveUserId(getCurrentUser().getId());
        Page<MessageUser> messageUserPageInfo = messageService.studentPage(messageRequestVM);
        List<Integer> ids = messageUserPageInfo.getContent().stream()
                .map(MessageUser::getMessageId)
                .collect(Collectors.toList());
        List<Message> messages = !ids.isEmpty() ? messageService.findAllById(ids) : Collections.emptyList();

        Page<MessageResponseVM> page = messageUserPageInfo.map(e -> {
            MessageResponseVM vm = modelMapper.map(e, MessageResponseVM.class);
            messages.stream()
                    .filter(d -> e.getMessageId().equals(d.getId()))
                    .findFirst()
                    .ifPresent(message -> {
                        vm.setTitle(message.getTitle());
                        vm.setContent(message.getContent());
                        vm.setSendUserName(message.getSendUserName());
                    });
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }

    @RequestMapping(value = "/message/unreadCount", method = RequestMethod.POST)
    public RestResponse unReadCount() {
        Integer count = messageService.unReadCount(getCurrentUser().getId());
        return RestResponse.ok(count);
    }

    @RequestMapping(value = "/message/read/{id}", method = RequestMethod.POST)
    public RestResponse read(@PathVariable Integer id) {
        messageService.read(id);
        return RestResponse.ok();
    }

}
