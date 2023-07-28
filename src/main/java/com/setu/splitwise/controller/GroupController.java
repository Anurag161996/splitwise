


package com.setu.splitwise.controller;

import com.setu.splitwise.constants.Urls;
import com.setu.splitwise.exceptions.GroupNotFoundException;
import com.setu.splitwise.exceptions.GroupValidationException;
import com.setu.splitwise.exceptions.UserNotFoundException;
import com.setu.splitwise.exceptions.UserValidationException;
import com.setu.splitwise.model.Group;
import com.setu.splitwise.model.input.GroupInput;
import com.setu.splitwise.model.view.GroupView;
import com.setu.splitwise.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Urls.GROUP)
@Slf4j
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/{id}")
    public ResponseEntity<GroupView> getGroupDetail(@PathVariable Long id) throws GroupValidationException {
        GroupView groupDetailInfo = groupService.getDetailedGroupById(id);
        return ResponseEntity.ok(groupDetailInfo);
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody GroupInput groupInput) throws GroupValidationException, UserNotFoundException {
        Group group = groupService.createGroup(groupInput);
        return ResponseEntity.ok(group);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody GroupInput groupInput) throws GroupValidationException, GroupNotFoundException {
        Group group = groupService.updateGroup(id, groupInput);
        return ResponseEntity.ok(group);
    }

    @ExceptionHandler({GroupValidationException.class, UserValidationException.class})
    public ResponseEntity<String> handleClientException(Exception e) {
        log.warn("Exception ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, GroupNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(Exception e) {
        log.warn("Exception ", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.warn("Something Went Wrong", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
