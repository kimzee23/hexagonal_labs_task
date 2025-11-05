package org.enums.labs_hexagonal.adapter.in.web;

import org.enums.labs_hexagonal.application.port.in.HealthCheckUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/.well-known")
@RequiredArgsConstructor
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

    private final HealthCheckUseCase healthCheckUseCase;

    @Operation(summary = "Health check", description = "Check if the service is running")
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        var response = healthCheckUseCase.checkHealth();
        return ResponseEntity.ok(response);
    }
}