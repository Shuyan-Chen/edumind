package com.shuyan.edumind.repository;

import com.shuyan.edumind.domain.MessageUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageUserRepository extends JpaRepository<MessageUser, Integer> {
    Page<MessageUser> findByReceiveUserId(Integer receiveUserId, Pageable pageable);
    List<MessageUser> findByMessageIdIn(List<Integer> ids);

    Integer countByReceiveUserIdAndReadedFalse(Integer userId);



}
