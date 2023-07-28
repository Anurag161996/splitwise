package com.setu.splitwise.repository;

import com.setu.splitwise.model.Group;
import com.setu.splitwise.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    List<UserGroup> findByUserId(Long userId);

    List<UserGroup> findByGroupId(Long groupId);

    UserGroup findByGroupIdAndUserId(Long groupId, Long userId);

}