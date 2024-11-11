package com.shuyan.edumind.service;


import com.shuyan.edumind.domain.Message;
import com.shuyan.edumind.domain.MessageUser;
import com.shuyan.edumind.viewmodel.admin.message.MessagePageRequestVM;
import com.shuyan.edumind.viewmodel.student.user.MessageRequestVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    List<Message> findAllById(List<Integer> ids);

    Page<MessageUser> studentPage(MessageRequestVM requestVM);

    Page<Message> page(MessagePageRequestVM requestVM, Pageable pageable);

    List<MessageUser> findByMessageIds(List<Integer> ids);

    void sendMessage(Message message, List<MessageUser> messageUsers);

    void read(Integer id);

    Integer unReadCount(Integer userId);

    Message messageDetail(Integer id);
}
