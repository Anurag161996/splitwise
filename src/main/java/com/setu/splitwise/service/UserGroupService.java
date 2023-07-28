package com.setu.splitwise.service;

import com.setu.splitwise.model.UserGroup;

import java.util.List;
import java.util.Set;

public interface UserGroupService {
    void createUserGroup(Set<Long> userIds, Long groupId);

    List<UserGroup> findUserGroupByUserId(Long userId);

    UserGroup findByGroupIdAndUserId(Long groupId, Long userId);

    List<UserGroup> findUserGroupByGroupId(Long groupId);
}
