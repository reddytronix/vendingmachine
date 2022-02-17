package com.reddytronix.vendingmachine.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.reddytronix.vendingmachine.exception.UserNotFoundException;
import com.reddytronix.vendingmachine.exception.UserUnauthorisedException;
import com.reddytronix.vendingmachine.model.User;
import com.reddytronix.vendingmachine.repository.UserRepository;
import com.reddytronix.vendingmachine.security.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthService authService;

    public User createUser(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(final String username, final User user) {

        if (!authService.getCurrentAuthenticatedUsername().equals(username)) {
            throw new UserUnauthorisedException(
                "User is not authorised to update user details of username : " + username);
        }
        final User userToBeUpdated = findUserByUsername(username);
        userToBeUpdated.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(userToBeUpdated);
    }

    public long deposit(final int coinValue) {
        final User user = authService.getCurrentAuthenticatedUser();
        final long deposit = user.getDeposit() + coinValue;
        user.setDeposit(deposit);
        userRepository.save(user);
        return deposit;
    }

    public void reset() {
        final User user = authService.getCurrentAuthenticatedUser();
        user.setDeposit(0L);
        userRepository.save(user);
    }

    public User findUserByUsername(final String username) {
        return userRepository.findByUsername(username)
                             .orElseThrow(() -> new UserNotFoundException(
                                 "User does not exists. Provided username : " + username));
    }

    public User findUserById(final Long id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new UserNotFoundException("User does not exists. Provided id : " + id));
    }

    public void saveUser(final User user) {
        userRepository.save(user);
    }

    public void deleteUser(final String username) {
        final User userToBeDeleted = findUserByUsername(username);

        userRepository.delete(userToBeDeleted);
    }
}
