package org.enums.labs_hexagonal.application.port.in;

import org.enums.labs_hexagonal.adapter.in.dto.response.HealthResponse;

public interface HealthCheckUseCase {
    HealthResponse checkHealth();
}
