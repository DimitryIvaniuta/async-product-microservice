package com.product.service;

import com.product.model.User;
import com.product.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Async("taskExecutor")
    public CompletableFuture<User> fetchUserDetailsAsync(Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay(600);
            return userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        });
    }

    private void simulateDelay(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<List<User>> getAllUsersAsync() {
        return CompletableFuture.supplyAsync(() -> userRepository.findUsers());
    }

    @Async("taskExecutor")
    public CompletableFuture<User> getUserByIdAsync(String userId) {
        return CompletableFuture.supplyAsync(() -> userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Async("taskExecutor")
    public CompletableFuture<User> createUserAsync(User user) {
        return CompletableFuture.supplyAsync(() -> userRepository.save(user));
    }

    @Async("taskExecutor")
    public CompletableFuture<User> updateUserAsync(String userId, User updatedUser) {
        return CompletableFuture.supplyAsync(() -> {
            User existingUser = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            return userRepository.save(existingUser);
        });
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> deleteUserAsync(String userId) {
        return CompletableFuture.runAsync(() -> {
            userRepository.deleteById(Long.valueOf(userId));
        });
    }

}
