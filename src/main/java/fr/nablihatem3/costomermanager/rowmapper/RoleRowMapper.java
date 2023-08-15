package fr.nablihatem3.costomermanager.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import fr.nablihatem3.costomermanager.domain.Role;
/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 14/08/2023
 */
public class RoleRowMapper implements RowMapper<Role> {

    @Override
    @Nullable
    public Role mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Role.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .Permission(resultSet.getString("permission"))
                .build();
    }
    
}
