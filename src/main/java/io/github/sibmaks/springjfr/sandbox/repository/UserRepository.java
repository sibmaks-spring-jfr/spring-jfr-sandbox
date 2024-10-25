package io.github.sibmaks.springjfr.sandbox.repository;

import io.github.sibmaks.springjfr.sandbox.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User JPA repository
 *
 * @author sibmaks
 * @since 0.0.1
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * Check if user exists by username
     *
     * @param username username
     * @return true if user exists, false otherwise
     */
    boolean existsByUsername(String username);
}
