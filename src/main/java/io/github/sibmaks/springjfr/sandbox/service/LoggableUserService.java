package io.github.sibmaks.springjfr.sandbox.service;

import io.github.sibmaks.springjfr.sandbox.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class LoggableUserService implements UserService {
    private final UserServiceImpl userService;

    @Override
    public boolean isUserExists(String username) {
        log.info("isUserExists - username: {}", username);
        return userService.isUserExists(username);
    }

    @Override
    public UserDto getById(long id) {
        log.info("getById - id: {}", id);
        return userService.getById(id);
    }

    @Override
    public void createUser(String username, Long groupId) {
        log.info("createUser - username: {}, groupId: {}", username, groupId);
        userService.createUser(username, groupId);
    }
}
