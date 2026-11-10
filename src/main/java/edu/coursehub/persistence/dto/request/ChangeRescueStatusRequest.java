package edu.coursehub.persistence.dto.request;

import edu.coursehub.persistence.domain.RescueStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeRescueStatusRequest(
        @NotNull(message = "Status is required")
        RescueStatus status
) {
}