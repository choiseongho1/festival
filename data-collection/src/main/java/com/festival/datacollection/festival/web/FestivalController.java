package com.festival.datacollection.festival.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data-collection")
public class FestivalController {

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Data-Collection Server is running successfully";
    }
}
