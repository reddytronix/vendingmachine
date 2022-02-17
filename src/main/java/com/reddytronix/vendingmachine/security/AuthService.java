package com.reddytronix.vendingmachine.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.reddytronix.vendingmachine.exception.UserNotFoundException;
import com.reddytronix.vendingmachine.model.User;
import com.reddytronix.vendingmachine.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User getCurrentAuthenticatedUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getDetails() instanceof VmUserDetails) {
            return ((VmUserDetails) authentication.getDetails()).getWrappedUser();
        }
        final String username = authentication.getName();
        return userRepository.findByUsername(username)
                             .orElseThrow(() -> new UserNotFoundException("User with username : " + username + " does not exists"));
    }

    public String getCurrentAuthenticatedUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
