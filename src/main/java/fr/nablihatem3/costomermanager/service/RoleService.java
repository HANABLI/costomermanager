package fr.nablihatem3.costomermanager.service;

import fr.nablihatem3.costomermanager.domain.Role;
import fr.nablihatem3.costomermanager.domain.User;

public interface RoleService {

    public Role getRoleByUserId(Long id);
}
