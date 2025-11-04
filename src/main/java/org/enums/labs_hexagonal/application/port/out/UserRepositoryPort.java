package org.enums.labs_hexagonal.application.port.out;


import org.enums.labs_hexagonal.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    boolean existsByEmail(String email);
}

