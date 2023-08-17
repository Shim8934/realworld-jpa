package com.io.realworldjpa.domain.user.service;

import com.io.realworldjpa.domain.user.entity.Email;
import com.io.realworldjpa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(Email email);

    boolean existsByEmail(Email email);

    Optional<User> findById(Long id);

    Optional<User> findByProfileUsername(String target);
}
