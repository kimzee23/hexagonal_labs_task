package org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response")
public class AuthResponse {
    @Schema(description = "Response message", example = "PENDING_VERIFICATION")
    private String message;

    @Schema(description = "User email", example = "talent@example.com")
    private String email;

    @Schema(description = "Session token for authenticated requests", example = "abc123...")
    private String sessionToken;
}