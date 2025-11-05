package org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private String email;
    private String transcript;
    private String statementOfPurpose;
    private int completeness;
    private java.util.List<String> missingFields;
}