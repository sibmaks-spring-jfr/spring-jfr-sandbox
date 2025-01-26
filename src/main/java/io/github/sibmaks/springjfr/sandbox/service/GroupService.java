package io.github.sibmaks.springjfr.sandbox.service;

import io.github.sibmaks.springjfr.sandbox.api.dto.GroupDto;
import io.github.sibmaks.springjfr.sandbox.entity.GroupEntity;
import io.github.sibmaks.springjfr.sandbox.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@AllArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
   /* private final GroupService groupService1;

    public GroupService(
            GroupRepository groupRepository,
            @Qualifier("groupService")
            GroupService groupService1) {
        this.groupRepository = groupRepository;
        this.groupService1 = groupService1;
    }*/

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
