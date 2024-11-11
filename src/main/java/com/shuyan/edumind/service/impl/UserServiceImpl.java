package com.shuyan.edumind.service.impl;

import com.shuyan.edumind.domain.User;
import com.shuyan.edumind.domain.other.KeyValue;
import com.shuyan.edumind.event.OnRegistrationCompleteEvent;
import com.shuyan.edumind.exception.BusinessException;
import com.shuyan.edumind.repository.UserRepository;
import com.shuyan.edumind.service.UserService;
import com.shuyan.edumind.viewmodel.admin.user.UserPageRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ApplicationEventPublisher eventPublisher){
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public Page<User> userPage(UserPageRequestVM requestVM, Pageable pageable) {
        return userRepository.findByUserNameAndRole(requestVM.getUserName(),requestVM.getRole(), pageable);
    }

    @Override
    public List<User> findByIdIn(List<Integer> ids) { return userRepository.findByIdIn(ids);}

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<KeyValue> findByUserName(String username) {return userRepository.findByUsername(username);}

    @Override
    public User findByUsername(String username) { return userRepository.findByUserName(username);}

    @Override
    public User save(User record) {
        return userRepository.save(record);
    }


    @Override
    @Transactional
    public void changePicture(Integer userId, String imagePath) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setImagePath(imagePath);
            userRepository.save(existingUser);
        }
    }

}
