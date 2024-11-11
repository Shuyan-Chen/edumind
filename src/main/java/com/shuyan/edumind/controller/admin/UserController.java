package com.shuyan.edumind.controller.admin;

import com.shuyan.edumind.base.BaseApiController;
import com.shuyan.edumind.base.RestResponse;
import com.shuyan.edumind.domain.User;
import com.shuyan.edumind.domain.UserEventLog;
import com.shuyan.edumind.domain.enums.UserStatusEnum;
import com.shuyan.edumind.domain.other.KeyValue;
import com.shuyan.edumind.service.AuthenticationService;
import com.shuyan.edumind.service.UserEventLogService;
import com.shuyan.edumind.service.UserService;
import com.shuyan.edumind.utility.DateTimeUtil;
import com.shuyan.edumind.viewmodel.admin.user.*;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController("AdminUserController")
@RequestMapping(value = "/api/admin/user")
public class UserController extends BaseApiController {

    private final UserService userService;
    private final UserEventLogService userEventLogService;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService, UserEventLogService userEventLogService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.userEventLogService = userEventLogService;
        this.authenticationService = authenticationService;
    }


    @RequestMapping(value = "/page/list", method = RequestMethod.POST)
    public RestResponse<Page<UserResponseVM>> pageList(@RequestBody UserPageRequestVM model) {
        PageRequest pageable = PageRequest.of(model.getPageIndex(), model.getPageSize());
        Page<User> pageInfo = userService.userPage(model,pageable);
        Page<UserResponseVM> page = pageInfo.map( d -> UserResponseVM.from(d));
        return RestResponse.ok(page);
    }


    @RequestMapping(value = "/event/page/list", method = RequestMethod.POST)
    public RestResponse<Page<UserEventLogVM>> eventPageList(@RequestBody UserEventPageRequestVM model) {
        PageRequest pageRequest = PageRequest.of(model.getPageIndex(), model.getPageSize());
        Page<UserEventLog> pageInfo = userEventLogService.page(model, pageRequest);
        Page<UserEventLogVM> page = pageInfo.map( d -> {
            UserEventLogVM vm = modelMapper.map(d, UserEventLogVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(d.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }

    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    public RestResponse<UserResponseVM> select(@PathVariable Integer id) {
        User user = userService.findById(id);
        UserResponseVM userVm = UserResponseVM.from(user);
        return RestResponse.ok(userVm);
    }

    @RequestMapping(value = "/current", method = RequestMethod.POST)
    public RestResponse<UserResponseVM> current() {
        User user = getCurrentUser();
        UserResponseVM userVm = UserResponseVM.from(user);
        return RestResponse.ok(userVm);
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RestResponse<User> edit(@RequestBody @Valid UserCreateVM model) {
        if (model.getId() == null) {
            User existUser = userService.findByUsername(model.getUserName());
            if (existUser != null) {
                return new RestResponse<>(2, "用户已存在");
            }
            if (StringUtils.isBlank(model.getPassword())) {
                return new RestResponse<>(3, "密码不能为空");
            }
        }
        if (StringUtils.isBlank(model.getBirthDay())) {
            model.setBirthDay(null);
        }
        User user = modelMapper.map(model, User.class);

        if (model.getId() == null) {
            String encodePwd = authenticationService.pwdEncode(model.getPassword());
            user.setPassword(encodePwd);
            user.setUserUuid(UUID.randomUUID().toString());
            user.setCreateTime(new Date());
            user.setLastActiveTime(new Date());
            user.setDeleted(false);
            userService.save(user);
        } else {
            if (!StringUtils.isBlank(model.getPassword())) {
                String encodePwd = authenticationService.pwdEncode(model.getPassword());
                user.setPassword(encodePwd);
            }
            user.setModifyTime(new Date());
            userService.save(user);
        }
        return RestResponse.ok(user);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResponse update(@RequestBody @Valid UserUpdateVM model) {
        User user = userService.findById(getCurrentUser().getId());
        modelMapper.map(model, user);
        user.setModifyTime(new Date());
        userService.save(user);
        return RestResponse.ok();
    }


    @RequestMapping(value = "/changeStatus/{id}", method = RequestMethod.POST)
    public RestResponse<Integer> changeStatus(@PathVariable Integer id) {
        User user = userService.findById(id);
        UserStatusEnum userStatusEnum = UserStatusEnum.fromCode(user.getStatus());
        Integer newStatus = userStatusEnum == UserStatusEnum.Enable ? UserStatusEnum.Disable.getCode() : UserStatusEnum.Enable.getCode();
        user.setStatus(newStatus);
        user.setModifyTime(new Date());
        userService.save(user);
        return RestResponse.ok(newStatus);
    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Integer id) {
        User user = userService.findById(id);
        user.setDeleted(true);
        userService.save(user);
        return RestResponse.ok();
    }

    @RequestMapping(value = "/selectByUserName", method = RequestMethod.POST)
    public RestResponse<List<KeyValue>> selectByUserName(@RequestBody String userName) {
        List<KeyValue> keyValues = userService.findByUserName(userName);
        return RestResponse.ok(keyValues);
    }

}
