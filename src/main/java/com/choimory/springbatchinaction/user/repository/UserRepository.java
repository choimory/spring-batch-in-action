package com.choimory.springbatchinaction.user.repository;

import com.choimory.springbatchinaction.user.entity.User;
import com.choimory.springbatchinaction.user.querydsl.CustomUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
}
