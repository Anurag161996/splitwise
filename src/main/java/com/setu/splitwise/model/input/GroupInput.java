package com.setu.splitwise.model.input;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.Set;

@Data
public class GroupInput {

    private Long groupId;

    private String groupName;

    private String groupDescription;

    Set<Long> userIds;

}
