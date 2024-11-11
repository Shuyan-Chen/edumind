package com.shuyan.edumind.repository;

import com.shuyan.edumind.domain.Message;
import com.shuyan.edumind.domain.MessageUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Modifying
    @Query("UPDATE Message m SET m.readCount = m.readCount + 1 WHERE m.id = :messageId")
    void incrementReadCount(@Param("messageId") Integer messageId);

    Page<Message> findBySendUserName(String sendUserName, Pageable pageable);


}
