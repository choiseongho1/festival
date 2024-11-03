package com.festival.group.web;

import com.festival.group.domain.Group;
import com.festival.group.dto.GroupSaveDto;
import com.festival.group.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    private GroupService groupService;


    @GetMapping("/health-check")
    public String healthCheck() {
        return "Group Server is running successfully";
    }



    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestBody GroupSaveDto groupSaveDto) {
        Group group = groupService.createGroup(groupSaveDto);
        return ResponseEntity.ok(group);
    }
}
