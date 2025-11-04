package org.enums.labs_hexagonal.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.enums.labs_hexagonal.application.port.in.HealthCheckUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/.well-known")
@RequiredArgsConstructor
public class HealthController {

    private final HealthCheckUseCase healthCheckUseCase;

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        var response = healthCheckUseCase.checkHealth();
        return ResponseEntity.ok(response);
    }
}