package com.reddytronix.vendingmachine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reddytronix.vendingmachine.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(final String username);
}