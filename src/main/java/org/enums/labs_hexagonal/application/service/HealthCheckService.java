package org.enums.labs_hexagonal.application.service;

import org.enums.labs_hexagonal.adapter.in.dto.response.HealthResponse;
import org.enums.labs_hexagonal.application.port.in.HealthCheckUseCase;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService implements HealthCheckUseCase {

    @Override
    public HealthResponse checkHealth() {
        return HealthResponse.builder()
                .status("ok")
                .build();
    }
}