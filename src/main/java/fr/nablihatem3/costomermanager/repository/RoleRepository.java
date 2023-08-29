package fr.nablihatem3.costomermanager.repository;

import java.util.Collection;

import fr.nablihatem3.costomermanager.domain.Role;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 10/08/2023
 */
public interface RoleRepository<T extends Role> {
       /* CRUD : Operations de base */
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(long id);

    /* More Complex Operations */ 

    void addRoleToUser(Long userId, String roleName);
    Role getRoleByUserId(Long userId);
    Role getRoleByUserEmail(String email);
    void updateUserRole(Long userId, String roleName);
}
