package com.festival.group.enums;

public enum JoinRequestStatus {
    PENDING("대기중"),
    ACCEPTED("수락됨"),
    REJECTED("거절됨");

    private final String description;

    JoinRequestStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 