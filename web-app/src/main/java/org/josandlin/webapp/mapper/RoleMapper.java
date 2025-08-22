package org.josandlin.nacbackend2groupjosandlin.mapper;

import org.josandlin.nacbackend2groupjosandlin.dto.RoleDTO;
import org.josandlin.nacbackend2groupjosandlin.entity.Role;
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
