package com.shuyan.edumind.service.impl;

import com.shuyan.edumind.domain.Message;
import com.shuyan.edumind.domain.MessageUser;
import com.shuyan.edumind.repository.MessageRepository;
import com.shuyan.edumind.repository.MessageUserRepository;
import com.shuyan.edumind.service.MessageService;
import com.shuyan.edumind.viewmodel.admin.message.MessagePageRequestVM;
import com.shuyan.edumind.viewmodel.student.user.MessageRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageUserRepository messageUserRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, MessageUserRepository messageUserRepository) {
        this.messageRepository = messageRepository;
        this.messageUserRepository = messageUserRepository;
    }

    @Override
    public List<Message> findAllById(List<Integer> ids) {
        return messageRepository.findAllById(ids);
    }

    @Override
    public Page<MessageUser> studentPage(MessageRequestVM requestVM) {
        PageRequest pageRequest = PageRequest.of(requestVM.getPageIndex(),requestVM.getPageSize());
        return messageUserRepository.findByReceiveUserId(requestVM.getReceiveUserId(), pageRequest);
    }

    @Override
    public Page<Message> page(MessagePageRequestVM requestVM, Pageable pageable) {
        return messageRepository.findBySendUserName(requestVM.getSendUserName(), pageable);
    }

    @Override
    public List<MessageUser> findByMessageIds(List<Integer> ids) {
        return messageUserRepository.findByMessageIdIn(ids);
    }

    @Override
    @Transactional
    public void sendMessage(Message message, List<MessageUser> messageUsers) {
        messageRepository.save(message);
        messageUsers.forEach(d -> d.setMessageId(message.getId()));
        messageUserRepository.save(messageUsers);
    }

    @Override
    @Transactional
    public void read(Integer id) {
        Optional<MessageUser> optionalMessageUser = messageUserRepository.findById(id);
        if (optionalMessageUser.isPresent()) {
            MessageUser messageUser = optionalMessageUser.get();
            if (messageUser.getReaded()) return;
            messageUser.setReaded(true);
            messageUser.setReadTime(new Date());
            messageUserRepository.save(messageUser);
            messageRepository.incrementReadCount(messageUser.getMessageId());
        }
    }

    @Override
    public Integer unReadCount(Integer userId) {
        return messageUserRepository.countByReceiveUserIdAndReadedFalse(userId);
    }

    @Override
    public Message messageDetail(Integer id) {
        MessageUser messageUser = messageUserRepository.selectByPrimaryKey(id);
        return messageRepository.selectByPrimaryKey(messageUser.getMessageId());
    }

}
