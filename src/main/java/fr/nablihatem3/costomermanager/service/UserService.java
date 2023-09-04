package fr.nablihatem3.costomermanager.service;

import fr.nablihatem3.costomermanager.domain.User;
import fr.nablihatem3.costomermanager.dto.UserDTO;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 14/08/2023
 */
public interface UserService {

    public UserDTO createUser(User user);

    public UserDTO getUserByEmail(String user);

    public void sendVerificationCode(UserDTO userDTO);

    UserDTO verifyCode(String email, String code);

    void resetPassword(String email);

    UserDTO verifyPasswordKey(String key);

    void renewPassword(String key, String password, String confirmPassword);

    UserDTO verifyAccountUrl(String key);
}
