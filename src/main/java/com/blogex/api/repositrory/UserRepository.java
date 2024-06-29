package com.blogex.api.repositrory;

import com.blogex.api.domain.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Users, Long>{

    // 아이디와 비밀번호 일치
    Optional<Users> findByEmailAndPassword(String email, String password);

    Optional<Users> findByEmail(String email);
}
