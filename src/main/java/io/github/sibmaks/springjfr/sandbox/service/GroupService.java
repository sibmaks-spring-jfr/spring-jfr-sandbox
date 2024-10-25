package io.github.sibmaks.springjfr.sandbox.service;

import io.github.sibmaks.springjfr.sandbox.entity.GroupEntity;
import io.github.sibmaks.springjfr.sandbox.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupEntity getById(long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Group %d not found", groupId)));
    }
}
