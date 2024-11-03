package com.festival.match.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Match Server is running successfully";
    }
}
