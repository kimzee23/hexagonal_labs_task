package org.enums.labs_hexagonal.adapter.out.persistence;

import org.enums.labs_hexagonal.adapter.out.persistence.entity.UserEntity;
import org.enums.labs_hexagonal.adapter.out.persistence.mapper.PersistenceMapper;
import org.enums.labs_hexagonal.adapter.out.persistence.repository.UserJpaRepository;
import org.enums.labs_hexagonal.application.port.out.UserRepositoryPort;
import org.enums.labs_hexagonal.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository userRepository;
    private final PersistenceMapper mapper;

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toUserEntity(user);
        UserEntity savedEntity = userRepository.save(entity);
        return mapper.toUser(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(mapper::toUser);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id)
                .map(mapper::toUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}