package com.kernel.auditoriums.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.kernel.auditoriums.entity.Building;
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
    public ResponseEntity<List<Group>> getGroups(@RequestParam(value = "name", required = false) String keyword) {
        return groupService.getGroups(keyword);
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

    @PutMapping("/{id}")
    @JsonView(Views.Default.class)
    public ResponseEntity<Group> editGroup(@PathVariable("id") Group groupFromDb, @RequestBody Group group) {
        return groupService.editGroup(groupFromDb, group);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable("id") Group group) {
        groupService.deleteGroup(group);
    }
}
