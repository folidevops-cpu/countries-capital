package com.example.countrycapital.entity;

public enum TaskStatus {
    IN_PROGRESS("In Progress"),
    DELIVERED("Delivered"),
    CANCELED("Canceled");
    
    private final String displayName;
    
    TaskStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}