package com.io.realworldjpa.domain.user.service;

import com.io.realworldjpa.domain.user.entity.Follow;
import com.io.realworldjpa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFromAndTo(User from, User to);

}
