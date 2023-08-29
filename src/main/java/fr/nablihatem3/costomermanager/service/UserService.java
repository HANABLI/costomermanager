package fr.nablihatem3.costomermanager.service;

import fr.nablihatem3.costomermanager.domain.User;
import fr.nablihatem3.costomermanager.dto.UserDTO;
import jakarta.validation.constraints.Email;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 14/08/2023
 */
public interface UserService {

    public UserDTO createUser(User user);

    public UserDTO getUserByEmail(String user);

    public void sendVerificvationCode(UserDTO userDTO);

    UserDTO verifyCode(String email, String code);
}
