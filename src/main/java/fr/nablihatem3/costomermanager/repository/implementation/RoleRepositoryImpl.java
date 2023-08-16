package fr.nablihatem3.costomermanager.repository.implementation;

import static java.util.Map.of;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.nablihatem3.costomermanager.domain.Role;
import fr.nablihatem3.costomermanager.exception.ApiException;
import fr.nablihatem3.costomermanager.repository.RoleRepository;
import fr.nablihatem3.costomermanager.rowmapper.RoleRowMapper;

import static fr.nablihatem3.costomermanager.querry.RoleQuery.*;
import static fr.nablihatem3.costomermanager.enumeration.RoleType.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 11/08/2023
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role> {
    private final NamedParameterJdbcTemplate jdbc;
    
    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("Adding role {} to user id: {}", roleName, userId);
        try {
            Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("name", roleName), new RoleRowMapper());
            jdbc.update(INSERT_ROLE_TO_USER, of("userId", userId, "roleId", Objects.requireNonNull(role).getId()));

        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No role name found by name" + ROLE_USER.name());
        } catch (Exception exception) {
            throw new ApiException(roleName);
        }
        
    }

    @Override
    public Role create(Role data) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean delete(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Role get(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Role> list(int page, int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Role update(Role data) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Role getRoleByUserId(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoleByUserId'");
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoleByUserEmail'");
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserRole'");
    }
    
}
