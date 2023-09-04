package fr.nablihatem3.costomermanager.service.implementation;

import fr.nablihatem3.costomermanager.domain.Role;
import fr.nablihatem3.costomermanager.domain.User;
import fr.nablihatem3.costomermanager.dto.UserDTO;
import fr.nablihatem3.costomermanager.repository.RoleRepository;
import fr.nablihatem3.costomermanager.repository.UserRepository;
import fr.nablihatem3.costomermanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static fr.nablihatem3.costomermanager.dtomapper.UserDTOMapper.fromUser;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 14/08/2023
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;
    private final RoleRepository<Role> roleRepository;

    @Override
    public UserDTO createUser(User user) {
        return mapToUserDTO(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return mapToUserDTO(userRepository.getUserByEmail(email));
    }

    @Override
    public void sendVerificationCode(UserDTO userDTO) {
        userRepository.sendVerificationCode(userDTO);
    }

    @Override
    public UserDTO verifyCode(String email, String code) {
        return mapToUserDTO(userRepository.verifyCode(email, code));
    }

    @Override
    public void resetPassword(String email) {
        userRepository.resetPassword(email);
    }

    @Override
    public UserDTO verifyPasswordKey(String key) {
        return mapToUserDTO(userRepository.verifyPasswordKey(key));
    }

    @Override
    public void renewPassword(String key, String password, String confirmPassword) {
        userRepository.renewPassword(key, password, confirmPassword);
    }

    @Override
    public UserDTO verifyAccountUrl(String key) {
        return mapToUserDTO(userRepository.verifyAccountKey(key));
    }

    private UserDTO mapToUserDTO(User user) {
        return  fromUser(user, roleRepository.getRoleByUserId(user.getId()));
    }

}
