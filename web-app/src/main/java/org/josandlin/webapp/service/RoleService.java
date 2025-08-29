package org.josandlin.webapp.service;

import org.josandlin.library.entity.user.Role;

public interface RoleService {

    Role getRoleByName(String name);

    /*
    List<RoleDTO> findAllRoles();

    @Transactional
    RoleDTO createRole(RoleDTO roleDTO);

    @Transactional
    RoleDTO deleteRole(RoleDTO roleDTO);




 */
}
