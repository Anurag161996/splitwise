package com.setu.splitwise.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String bio;

    private String firstName;

    private String lastName;

    private String emailId;

    private String phoneNumber;

    private Long createdAt;

    private Long updatedAt;
}
