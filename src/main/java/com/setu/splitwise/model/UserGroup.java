package com.setu.splitwise.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class UserGroup {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long groupId;

    private Long userId;

    private Long createdAt;

}
