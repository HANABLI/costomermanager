package fr.nablihatem3.costomermanager.service.implementation;

import org.springframework.stereotype.Service;

import fr.nablihatem3.costomermanager.domain.User;
import fr.nablihatem3.costomermanager.dto.UserDTO;
import fr.nablihatem3.costomermanager.dtomapper.UserDTOMapper;
import fr.nablihatem3.costomermanager.repository.UserRepository;
import fr.nablihatem3.costomermanager.service.UserService;
import lombok.RequiredArgsConstructor;
/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 14/08/2023
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;


    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepository.create(user));
    }
    
}
