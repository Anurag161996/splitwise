package com.setu.splitwise.validators;

import com.setu.splitwise.exceptions.GroupValidationException;
import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.User;
import com.setu.splitwise.model.input.GroupInput;
import com.setu.splitwise.repository.GroupRepository;
import com.setu.splitwise.repository.UserRepository;
import com.setu.splitwise.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class GroupValidator {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    public void validateNewGroup(GroupInput groupInput) throws GroupValidationException, UserNotFoundException {
        if(Objects.isNull(groupInput))
            throw new GroupValidationException("Input is null");
        if (groupInput.getGroupId() != null)
            throw new GroupValidationException("Group ID should be null.");
        if(StringUtils.isNullOrEmpty(groupInput.getGroupName()))
            throw new GroupValidationException("Group Name cannot be null.");
        if(StringUtils.isNullOrEmpty(groupInput.getGroupDescription()))
            throw new GroupValidationException("Group Name cannot be null.");
        if(CollectionUtils.isEmpty(groupInput.getUserIds()))
            throw new GroupValidationException("User Ids cannot be null.");
        validateUserIds(groupInput.getUserIds());
    }

    public void validateId(Long id) throws GroupValidationException {
        if (Objects.isNull(id))
            throw new GroupValidationException("Group ID cannot be null.");
    }

    public void validateUserIds(Set<Long> userIds) throws UserNotFoundException {
        for (Long userId : userIds) {
            if (!userRepository.existsById(userId)) {
                throw new UserNotFoundException("User with ID " + userId + " not found.");
            }
        }
    }

    public void validateExistingGroup(Long id, GroupInput groupInput) throws GroupValidationException {
        if(Objects.isNull(groupInput))
            throw new GroupValidationException("Input is null");
        validateId(id);
    }
}
