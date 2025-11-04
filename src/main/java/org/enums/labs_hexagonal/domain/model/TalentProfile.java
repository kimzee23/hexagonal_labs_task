package org.enums.labs_hexagonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TalentProfile {
    private UUID id;
    private UUID userId;
    private String transcript;
    private String statementOfPurpose;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public int calculateCompleteness() {
        boolean hasTranscript = transcript != null && !transcript.trim().isEmpty();
        boolean hasStatement = statementOfPurpose != null && !statementOfPurpose.trim().isEmpty();

        if (hasTranscript && hasStatement) return 100;
        if (hasTranscript || hasStatement) return 50;
        return 0;
    }

    public List<String> getMissingFields() {
        List<String> missing = new java.util.ArrayList<>();
        if (transcript == null || transcript.trim().isEmpty()) {
            missing.add("transcript");
        }
        if (statementOfPurpose == null || statementOfPurpose.trim().isEmpty()) {
            missing.add("statementOfPurpose");
        }
        return missing;
    }
}