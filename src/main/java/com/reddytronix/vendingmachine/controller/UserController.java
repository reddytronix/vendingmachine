package com.reddytronix.vendingmachine.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.reddytronix.vendingmachine.mapper.UserMapper;
import com.reddytronix.vendingmachine.model.User;
import com.reddytronix.vendingmachine.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/user")
    public ResponseEntity<UserRepresentation> registerUser(@RequestBody @Valid UserRepresentation userRepresentation) {
        final User user = userMapper.fromUserRepresentation(userRepresentation);
        final User newUser = userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserRepresentation(newUser));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<UserRepresentation> getUser(@PathVariable final String username) {
        final User user = userService.findUserByUsername(username);
        return ResponseEntity.ok(userMapper.toUserRepresentation(user));
    }

    @PutMapping("/user/{username}")
    public ResponseEntity<UserRepresentation> updateUser(@PathVariable final String username,
                                                         @RequestBody final UserRepresentation userRepresentation) {
        final User user = userService.findUserByUsername(username);
        final User updatedUser = userService.updateUser(username, user);
        return ResponseEntity.ok(userMapper.toUserRepresentation(updatedUser));
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<UserRepresentation> deleteUser(@PathVariable final String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}