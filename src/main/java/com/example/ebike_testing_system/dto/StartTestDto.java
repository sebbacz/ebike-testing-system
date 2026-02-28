package com.example.ebike_testing_system.dto;

import com.example.ebike_testing_system.model.TestType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record StartTestDto(

        @NotNull
//        @Pattern(regexp = "STRESS|EFFICIENCY|MANUAL|FAST", message = "Allowed types: STRESS/EFFICIENCY/MANUAL/FAST")
        TestType type,

        @NotNull
        Integer batteryCapacity,
        @NotNull
        Integer maxSupport,
        @NotNull
        Integer enginePowerMax,
        @NotNull
        Integer enginePowerNominal,
        @NotNull
        Integer engineTorque,

        @NotNull
        Integer ebikeId,

        @NotNull
        Integer customerId,

        Integer technicianId,

        @Nullable
        String notes       // Optional field
) {
}
