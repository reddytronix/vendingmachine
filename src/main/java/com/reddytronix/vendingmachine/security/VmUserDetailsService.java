package com.reddytronix.vendingmachine.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.reddytronix.vendingmachine.model.User;
import com.reddytronix.vendingmachine.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VmUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        final User user = userService.findUserByUsername(username);
        return new VmUserDetails(user);
    }
}
