package io.github.sibmaks.springjfr.sandbox.controller;

import io.github.sibmaks.springjfr.sandbox.api.dto.UserDto;
import io.github.sibmaks.springjfr.sandbox.api.rq.CreateUserRq;
import io.github.sibmaks.springjfr.sandbox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/controller/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;

    @GetMapping(
            path = "/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserDto getById(@PathVariable("userId") Long userId) {
        return userService.getById(userId);
    }

    @PostMapping(
            path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void addUser(@RequestBody CreateUserRq rq) {
        userService.createUser(
                rq.getUsername(),
                rq.getGroupId()
        );
    }

}
