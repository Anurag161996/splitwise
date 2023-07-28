package com.setu.splitwise.model.view;

import com.setu.splitwise.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GroupView {

    private Long groupId;

    private String groupName;

    private String groupDescription;

    private Long createdAt;

    private Long updatedAt;

    private List<User> users;
}
