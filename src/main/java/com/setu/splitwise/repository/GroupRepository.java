package com.setu.splitwise.repository;

import com.setu.splitwise.model.Group;
import com.setu.splitwise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

}