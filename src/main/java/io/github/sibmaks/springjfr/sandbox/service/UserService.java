package io.github.sibmaks.springjfr.sandbox.service;

import io.github.sibmaks.springjfr.sandbox.dto.UserDto;

public interface UserService {
    boolean isUserExists(String username);

    UserDto getById(long id);

    void createUser(String username, Long groupId);
}
