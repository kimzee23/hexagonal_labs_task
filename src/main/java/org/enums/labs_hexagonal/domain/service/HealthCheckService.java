package org.enums.labs_hexagonal.domain.service;

import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.response.HealthResponse;
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