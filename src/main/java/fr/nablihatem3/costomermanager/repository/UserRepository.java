package fr.nablihatem3.costomermanager.repository;

import java.util.Collection;
import fr.nablihatem3.costomermanager.domain.User;
import fr.nablihatem3.costomermanager.dto.UserDTO;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 10/08/2023
 */
public interface UserRepository<T extends User> {
    /* CRUD : Operations de base */
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(long id);
    User getUserByEmail(String email);
    void sendVerificationCode(UserDTO userDTO);

    User verifyCode(String email, String code);
    /* More Complex Operations */
}

