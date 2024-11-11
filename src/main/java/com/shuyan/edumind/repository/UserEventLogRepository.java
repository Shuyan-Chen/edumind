package com.shuyan.edumind.repository;

import com.shuyan.edumind.domain.UserEventLog;
import com.shuyan.edumind.domain.other.KeyValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserEventLogRepository extends JpaRepository<UserEventLog, Integer> {
    List<UserEventLog> findByUserId(Integer userId);

    Page<UserEventLog> findByUserIdAndUserName(Integer userId, String userName, Pageable pageable);
    List<KeyValue> countByCreateTimeBetween(Date start, Date end);

}
