package com.abranlezama.fullstackchatapplication.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TokenResponse(
        @NotBlank String jwtToken
) {
}
