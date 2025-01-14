package fr.nablihatem3.costomermanager.dto;

import java.time.LocalDateTime;

import lombok.*;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 13/08/2023
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String firstName;  
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String title;
    private String bio;
    private String imageUrl;
    private boolean enabled;
    private boolean isNotLocked;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;
    private String roleName;
    private String permissions;
}
