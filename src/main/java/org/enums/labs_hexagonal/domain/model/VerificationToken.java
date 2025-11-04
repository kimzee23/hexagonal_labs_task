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
public class VerificationToken {
    private UUID id;
    private UUID userId;
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime usedAt;
    private LocalDateTime createdAt;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isUsed() {
        return usedAt != null;
    }

    public void markAsUsed() {
        this.usedAt = LocalDateTime.now();
    }
}