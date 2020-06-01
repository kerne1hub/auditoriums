package com.kernel.auditoriums.service;

import com.kernel.auditoriums.entity.Group;
import com.kernel.auditoriums.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository repository;

    @Autowired
    public GroupService(GroupRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<List<Group>> getGroups(String keyword) {
        if (keyword != null) {
            List<Group> groups = repository.findAllByNameContains(keyword);
            return ResponseEntity.ok(groups);
        }
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<Group> getGroupInfo(Group group) {
        if (group != null) {
            return ResponseEntity.ok(group);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Group> createGroup(Group group) {
        Group savedGroup = repository.save(group);
        return new ResponseEntity<>(savedGroup, HttpStatus.CREATED);
    }

    public void deleteGroup(Group group) {
        repository.delete(group);
    }

    public ResponseEntity<Group> editGroup(Group groupFromDb, Group group) {
        groupFromDb.setName(group.getName());
        return ResponseEntity.ok(repository.save(groupFromDb));
    }
}
