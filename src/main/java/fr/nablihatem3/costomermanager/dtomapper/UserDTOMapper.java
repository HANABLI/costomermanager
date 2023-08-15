package fr.nablihatem3.costomermanager.dtomapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


import fr.nablihatem3.costomermanager.domain.User;
import fr.nablihatem3.costomermanager.dto.UserDTO;
/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 13/08/2023
 */

@Component
public class UserDTOMapper {
    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}
