package org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthResponse {
    private String status;

    public HealthResponse(String ok, String serviceIsRunning) {
    }
}
