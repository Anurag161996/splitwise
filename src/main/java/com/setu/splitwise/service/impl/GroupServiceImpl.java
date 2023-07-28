package com.setu.splitwise.service.impl;

import com.setu.splitwise.exceptions.GroupNotFoundException;
import com.setu.splitwise.exceptions.GroupValidationException;
import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.model.Group;
import com.setu.splitwise.model.User;
import com.setu.splitwise.model.UserGroup;
import com.setu.splitwise.model.input.GroupInput;
import com.setu.splitwise.model.view.GroupView;
import com.setu.splitwise.repository.GroupRepository;
import com.setu.splitwise.service.GroupService;
import com.setu.splitwise.service.UserGroupService;
import com.setu.splitwise.service.UserService;
import com.setu.splitwise.utils.StringUtils;
import com.setu.splitwise.validators.GroupValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupValidator groupValidator;

    @Autowired
    private UserGroupService userGroupService;

    /*
     Using Transactional Annotation to either run both the db query
     or roll back
     */

    @Override
    @Transactional
    public Group createGroup(GroupInput groupInput) throws GroupValidationException, UserNotFoundException {
        groupValidator.validateNewGroup(groupInput);
        Group group = groupRepository.save(mapInputToGroupModel(groupInput));
        userGroupService.createUserGroup(groupInput.getUserIds(), group.getId());
        return group;

    }

    @Override
    public Group updateGroup(Long id, GroupInput input) throws GroupValidationException, GroupNotFoundException {

        groupValidator.validateExistingGroup(id, input);

        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found with ID: " + id));

        if (StringUtils.isNotNullOrEmpty(input.getGroupName()))
            existingGroup.setGroupName(input.getGroupName());

        if (StringUtils.isNotNullOrEmpty(input.getGroupDescription()))
            existingGroup.setGroupDescription(input.getGroupDescription());

        existingGroup.setUpdatedAt(System.currentTimeMillis());

        return groupRepository.save(existingGroup);
    }

    @Override
    public GroupView getDetailedGroupById(Long groupId) throws GroupValidationException {
        groupValidator.validateId(groupId);
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupValidationException("Group not found with ID: " + groupId));
        List<UserGroup> userGroups = userGroupService.findUserGroupByGroupId(group.getId());
        //Pagination can be used for userFetching
        List<User> users = !CollectionUtils.isEmpty(userGroups)
                ? userService.getAllUserById(userGroups.stream().map(item -> item.getUserId()).collect(Collectors.toList())) : new ArrayList<>();
        return GroupView.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupDescription(group.getGroupDescription())
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .users(users)
                .build();
    }

    @Override
    public Group getGroupsByIds(Long groupId) throws GroupValidationException {
        groupValidator.validateId(groupId);
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupValidationException("User not found with ID: " + groupId));
    }

    @Override
    public Map<Long, Group> getGroupsByIds(List<Long> groupIds) {
        return groupRepository.findAllById(groupIds).stream().collect(Collectors.toMap(Group::getId, group -> group));
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    private Group mapInputToGroupModel(GroupInput groupInput) {
        Group group = new Group();
        group.setGroupName(groupInput.getGroupName());
        group.setGroupDescription(groupInput.getGroupDescription());
        group.setCreatedAt(System.currentTimeMillis());
        group.setUpdatedAt(System.currentTimeMillis());
        return group;
    }
}
