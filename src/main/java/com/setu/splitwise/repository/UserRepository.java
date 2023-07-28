package com.setu.splitwise.repository;

import com.setu.splitwise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailId(String emailId);
    boolean existsByPhoneNumber(String phoneNumber);
}