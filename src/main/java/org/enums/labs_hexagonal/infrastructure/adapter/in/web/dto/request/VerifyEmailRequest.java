package org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Verify email request")
public class VerifyEmailRequest {
    @NotBlank(message = "Token is required")
    @Schema(description = "Verification token sent to email", example = "verification-token-123")
    private String token;
}