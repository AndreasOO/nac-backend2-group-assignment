package org.josandlin.webapp.mapper;

import org.josandlin.webapp.dto.RoleDTO;
import org.josandlin.webapp.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDTO toRoleDto(Role entity) {
        if (entity == null) {
            return null;
        }

        return new RoleDTO(entity.getId(), entity.getName());
    }

    public Role toRoleEntity(RoleDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Role(dto.getId(), dto.getName());
    }
}
