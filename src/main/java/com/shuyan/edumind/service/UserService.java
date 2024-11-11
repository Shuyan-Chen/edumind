package com.shuyan.edumind.service;

import com.shuyan.edumind.domain.User;
import com.shuyan.edumind.domain.other.KeyValue;
import com.shuyan.edumind.viewmodel.admin.user.UserPageRequestVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Page<User> userPage(UserPageRequestVM requestVM, Pageable pageable);

    List<User> findByIdIn(List<Integer> ids);

    User findById(Integer id);

    User findByUsername(String username);

    List<KeyValue> findByUserName(String username);

    User save(User user);

    void changePicture(Integer userId, String imagePath);


}
