package org.josandlin.library.mapper;


import org.josandlin.library.dto.RoleDTO;
import org.josandlin.library.entity.Role;
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
