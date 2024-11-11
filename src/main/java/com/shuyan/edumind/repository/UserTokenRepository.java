package com.shuyan.edumind.repository;

import com.shuyan.edumind.domain.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {
    UserToken findByToken(String token);

}
