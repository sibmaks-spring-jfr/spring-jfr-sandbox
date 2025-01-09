package io.github.sibmaks.springjfr.sandbox.controller;

import io.github.sibmaks.springjfr.sandbox.api.dto.GroupDto;
import io.github.sibmaks.springjfr.sandbox.api.rq.CreateGroupRq;
import io.github.sibmaks.springjfr.sandbox.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/controller/group")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupController {
    private final GroupService groupService;

    @GetMapping(
            path = "/{groupId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GroupDto getById(@PathVariable("groupId") Long groupId) {
        return groupService.getById(groupId);
    }

    @PostMapping(
            path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void addGroup(@RequestBody CreateGroupRq rq) {
        groupService.create(rq.getCode());
    }

}
