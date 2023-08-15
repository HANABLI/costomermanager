package fr.nablihatem3.costomermanager.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 10/08/2023
 */

 @Data
 @SuperBuilder
 @NoArgsConstructor
 @JsonInclude()
public class User {
    private Long id;
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;
    @NotEmpty(message = "Email name cannot be empty")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    private String address;
    private String phone;
    private String title;
    private String bio;
    private String imageUrl;
    private boolean enabled;
    private boolean isNotLocked;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;
}
