package org.enums.labs_hexagonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    private UUID id;
    private UUID userId;
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private boolean revoked;

    public boolean isValid() {
        return !revoked && LocalDateTime.now().isBefore(expiresAt);
    }

    public void revoke() {
        this.revoked = true;
    }
}