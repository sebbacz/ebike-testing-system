package com.example.ebike_testing_system.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class TestReportId implements Serializable {
    private UUID uuid;
    private LocalDateTime timestamp;

    public TestReportId() {}

    public TestReportId(UUID uuid, LocalDateTime timestamp) {
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestReportId that = (TestReportId) o;
        return Objects.equals(uuid, that.uuid) && (Objects.equals(timestamp, that.timestamp));
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, timestamp);
    }
}
