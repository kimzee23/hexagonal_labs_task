package org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private String transcript;
    private String statementOfPurpose;
}