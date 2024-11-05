package io.github.sibmaks.springjfr.sandbox.service;

import io.github.sibmaks.springjfr.sandbox.api.dto.UserDto;
import io.github.sibmaks.springjfr.sandbox.entity.UserEntity;
import io.github.sibmaks.springjfr.sandbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final GroupService groupService;

    @Override
    public boolean isUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDto getById(long id) {
        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User %d not found", id)));
        return UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .createAt(userEntity.getCreatedAt())
                .modifiedAt(userEntity.getModifiedAt())
                .build();
    }

    @Override
    public void createUser(String username, Long groupId) {
        var groupEntity = groupService.getEntityById(groupId);

        var creationDate = ZonedDateTime.now();
        var entity = UserEntity.builder()
                .username(username)
                .group(groupEntity)
                .createdAt(creationDate)
                .modifiedAt(creationDate)
                .build();
        userRepository.save(entity);
    }
}
