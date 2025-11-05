package org.enums.labs_hexagonal;


import org.enums.labs_hexagonal.application.port.in.HealthCheckUseCase;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.HealthController;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.response.HealthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HealthControllerTest {

    @Mock
    private HealthCheckUseCase healthCheckUseCase;

    @InjectMocks
    private HealthController healthController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void healthCheck_ShouldReturnOkResponse() {

        HealthResponse mockResponse = new HealthResponse("OK", "Service is running");
        when(healthCheckUseCase.checkHealth()).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = healthController.healthCheck();

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(mockResponse);

        verify(healthCheckUseCase, times(1)).checkHealth();
    }
}
