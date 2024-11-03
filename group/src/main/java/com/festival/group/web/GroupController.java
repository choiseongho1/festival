package com.festival.group.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Group Server is running successfully";
    }
}
