package fr.nablihatem3.costomermanager.service.implementation;

import fr.nablihatem3.costomermanager.domain.Role;
import fr.nablihatem3.costomermanager.repository.RoleRepository;
import fr.nablihatem3.costomermanager.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 27/08/2023
 */
@Service
@RequiredArgsConstructor
public class RooleServiceImpl implements RoleService {
    private final RoleRepository<Role> roleRepository;
    @Override
    public Role getRoleByUserId(Long id) {
        return roleRepository.getRoleByUserId(id);
    }
}
