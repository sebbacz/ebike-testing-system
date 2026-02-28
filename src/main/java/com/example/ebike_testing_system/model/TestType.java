package com.example.ebike_testing_system.model;

public enum TestType {
    STRESS,
    EFFICIENCY,
    MANUAL,
    FAST;

    public static TestType fromString(String genre) {
        try {
            return TestType.valueOf(genre.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid genre: " + genre);
        }
    }
}
