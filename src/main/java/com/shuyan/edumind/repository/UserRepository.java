package com.shuyan.edumind.repository;

import com.shuyan.edumind.domain.User;
import com.shuyan.edumind.domain.other.KeyValue;
import com.shuyan.edumind.viewmodel.admin.user.UserPageRequestVM;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT KeyValue(u.userName, u.id ) FROM User u WHERE u.userName = ?1")
    List<KeyValue> findByUsername(String username);

    @Query("SELECT u.userName from User u WHERE u.userName = ?1")
    User findByUserName(String username);

    List<User> findByIdIn(List<Integer> ids);

    Page<User> findByUserNameAndRole(String userName, Integer role, Pageable pageable);
}
