package com.kernel.auditoriums.controller;

import ch.qos.logback.classic.ViewStatusMessagesServlet;
import com.fasterxml.jackson.annotation.JsonView;
import com.kernel.auditoriums.entity.Group;
import com.kernel.auditoriums.entity.utils.Views;
import com.kernel.auditoriums.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    @JsonView(Views.Default.class)
    public ResponseEntity<List<Group>> getGroups() {
        return groupService.getGroups();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Group.class)
    public ResponseEntity<Group> getGroupInfo(@PathVariable("id") Group group) {
        return groupService.getGroupInfo(group);
    }

    @PostMapping
    @JsonView(Views.Default.class)
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable("id") Group group) {
        groupService.deleteGroup(group);
    }
}