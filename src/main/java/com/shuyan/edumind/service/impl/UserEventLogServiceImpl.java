package com.shuyan.edumind.service.impl;

import com.shuyan.edumind.domain.UserEventLog;
import com.shuyan.edumind.domain.other.KeyValue;
import com.shuyan.edumind.repository.UserEventLogRepository;
import com.shuyan.edumind.service.UserEventLogService;
import com.shuyan.edumind.utility.DateTimeUtil;
import com.shuyan.edumind.viewmodel.admin.user.UserEventPageRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserEventLogServiceImpl implements UserEventLogService {

    private final UserEventLogRepository userEventLogRepository;

    @Autowired
    public UserEventLogServiceImpl(UserEventLogRepository userEventLogRepository) {
        this.userEventLogRepository = userEventLogRepository;
    }

    @Override
    public List<UserEventLog> findByUserId(Integer id) {
        return userEventLogRepository.findByUserId(id);
    }

    @Override
    public Page<UserEventLog> page(UserEventPageRequestVM requestVM, Pageable pageable) {
        return userEventLogRepository.findByUserIdAndUserName(requestVM.getUserId(),requestVM.getUserName(), pageable);
    }

    @Override
    public List<Integer> findMonthCount() {
        Date startTime = DateTimeUtil.getMonthStartDay();
        Date endTime = DateTimeUtil.getMonthEndDay();
        List<KeyValue> mouthCount = userEventLogRepository.countByCreateTimeBetween(startTime, endTime);
        List<String> mothStartToNowFormat = DateTimeUtil.MonthStartToNowFormat();
        return mothStartToNowFormat.stream().map(md -> {
            KeyValue keyValue = mouthCount.stream().filter(kv -> kv.getName().equals(md)).findAny().orElse(null);
            return null == keyValue ? 0 : keyValue.getValue();
        }).collect(Collectors.toList());
    }

}
