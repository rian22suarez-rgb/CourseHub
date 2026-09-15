package edu.coursehub.persistence.dto.request;

import edu.coursehub.persistence.domain.RescueStatus;

public record ChangeRescueStatusRequest(
        RescueStatus status
) {
}