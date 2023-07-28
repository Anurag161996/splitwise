package com.setu.splitwise.service.impl;

import com.setu.splitwise.model.UserGroup;
import com.setu.splitwise.repository.UserGroupRepository;
import com.setu.splitwise.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    UserGroupRepository userGroupRepository;

    @Override
    public void createUserGroup(Set<Long> userIds, Long groupId) {
        List<UserGroup> userGroups = new ArrayList<>();
        userIds.forEach(item -> {
            UserGroup userGroup = new UserGroup();
            userGroup.setUserId(item);
            userGroup.setGroupId(groupId);
            userGroup.setCreatedAt(System.currentTimeMillis());
            userGroups.add(userGroup);
        });
        userGroupRepository.saveAll(userGroups);
    }

    @Override
    public List<UserGroup> findUserGroupByUserId(Long userId) {
       return userGroupRepository.findByUserId(userId);
    }

    @Override
    public UserGroup findByGroupIdAndUserId(Long groupId, Long userId) {
        return userGroupRepository.findByGroupIdAndUserId(groupId, userId);
    }

    @Override
    public List<UserGroup> findUserGroupByGroupId(Long groupId) {
        return userGroupRepository.findByGroupId(groupId);
    }

}
