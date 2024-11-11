package com.shuyan.edumind.service;

import com.shuyan.edumind.domain.UserEventLog;
import com.shuyan.edumind.viewmodel.admin.user.UserEventPageRequestVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserEventLogService {

    List<UserEventLog> findByUserId(Integer id);

    Page<UserEventLog> page(UserEventPageRequestVM requestVM, Pageable pageable);

    List<Integer> findMonthCount();
}
