package org.enums.labs_hexagonal.application.port.in;

import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.response.HealthResponse;

public interface HealthCheckUseCase {
    HealthResponse checkHealth();
}
