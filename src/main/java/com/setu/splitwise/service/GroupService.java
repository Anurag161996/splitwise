package com.setu.splitwise.service;

import com.setu.splitwise.exceptions.GroupNotFoundException;
import com.setu.splitwise.exceptions.GroupValidationException;
import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.model.Group;
import com.setu.splitwise.model.input.GroupInput;
import com.setu.splitwise.model.view.GroupView;

import java.util.List;
import java.util.Map;

public interface GroupService {
    Group createGroup(GroupInput groupInput) throws GroupValidationException, UserNotFoundException;

    Group updateGroup(Long id, GroupInput input) throws GroupValidationException, GroupNotFoundException;

    GroupView getDetailedGroupById(Long groupId) throws GroupValidationException;

    Group getGroupsByIds(Long groupId) throws GroupValidationException;

    Map<Long, Group> getGroupsByIds(List<Long> groupIds) throws GroupValidationException;

    List<Group> getAllGroups();
}
