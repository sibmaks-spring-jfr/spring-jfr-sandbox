package io.github.sibmaks.springjfr.sandbox.service;

import io.github.sibmaks.springjfr.sandbox.api.dto.GroupDto;
import io.github.sibmaks.springjfr.sandbox.entity.GroupEntity;
import io.github.sibmaks.springjfr.sandbox.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupDto getById(long groupId) {
        var entity = getEntityById(groupId);
        return GroupDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .createAt(entity.getCreatedAt())
                .build();
    }

    public GroupEntity getEntityById(long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Group %d not found", groupId)));
    }

    public void create(String code) {
        var entity = GroupEntity.builder()
                .code(code)
                .createdAt(ZonedDateTime.now())
                .build();
        groupRepository.save(entity);
    }
}
