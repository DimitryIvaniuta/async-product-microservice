package com.product.controller;

import com.product.dto.UserRequestDTO;
import com.product.dto.UserResponseDTO;
import com.product.model.User;
import com.product.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    // Asynchronous endpoint to get user details by user ID.
    @GetMapping("/{id}")
    public CompletableFuture<UserResponseDTO> getUser(@PathVariable("id") Long userId) {
        return userService.fetchUserDetailsAsync(userId)
                .exceptionally(ex -> {
                    log.error("Error fetching user: {}", ex.getMessage());
                    // Return a fallback user
                    return new User(userId, "Fallback User", "fallback@example.com");
                }).thenApply(user -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setId(user.getId());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    return dto;
                });
    }

    // Get all users
    @GetMapping
    public CompletableFuture<List<UserResponseDTO>> getAllUsers() {
        return userService.getAllUsersAsync().thenApply(users ->
                users.stream().map(this::toUserResponseDTO).collect(Collectors.toList())
        );
    }

    // Get user by ID
    @GetMapping("/get-user/{id}")
    public CompletableFuture<UserResponseDTO> getUserById(@PathVariable("id") String userId) {
        return userService.getUserByIdAsync(userId).thenApply(this::toUserResponseDTO);
    }

    // Create new user
    @PostMapping
    public CompletableFuture<UserResponseDTO> createUser(@RequestBody UserRequestDTO requestDTO) {
        User user = new User(requestDTO.getName(), requestDTO.getEmail());
        return userService.createUserAsync(user).thenApply(this::toUserResponseDTO);
    }

    // Update user
    @PutMapping("/{id}")
    public CompletableFuture<UserResponseDTO> updateUser(@PathVariable("id") String userId, @RequestBody UserRequestDTO requestDTO) {
        User updatedUser = new User(requestDTO.getName(), requestDTO.getEmail());
        return userService.updateUserAsync(userId, updatedUser).thenApply(this::toUserResponseDTO);
    }

    // Delete user
    @DeleteMapping("/{id}")
    public CompletableFuture<Void> deleteUser(@PathVariable("id") String userId) {
        return userService.deleteUserAsync(userId);
    }

    // Mapper method for User to UserResponseDTO
    private UserResponseDTO toUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

}
