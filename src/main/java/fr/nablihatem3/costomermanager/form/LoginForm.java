package fr.nablihatem3.costomermanager.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 27/08/2023
 */
@Data
public class LoginForm {
    @NotEmpty (message = "Email is empty")
    @Email (message = "Invalid email")
    private String email;
    @NotEmpty (message = "Your Password is empty")
    private String password;
}
