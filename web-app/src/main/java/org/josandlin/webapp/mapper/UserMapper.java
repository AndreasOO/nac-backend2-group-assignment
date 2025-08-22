package org.josandlin.webapp.mapper;

import org.josandlin.webapp.dto.UserDTO;
import org.josandlin.webapp.dto.UserSummaryDTO;
import org.josandlin.webapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    @Autowired
    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public UserSummaryDTO toUserSummaryDto(User entity) {
        if (entity == null) {
            return null;
        }

        return new UserSummaryDTO(entity.getId(), entity.getUsername());
    }

    public UserDTO toUserDto(User entity) {
        if (entity == null) {
            return null;
        }

        return new UserDTO(entity.getId(), entity.getUsername(), entity.getPassword(),
                entity.getRoles().stream().map(roleMapper::toRoleDto).toList());
    }

    public User toUserEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        return new User(dto.getId(), dto.getUsername(), dto.getPassword(),
                dto.getRoles().stream().map(roleMapper::toRoleEntity).toList());
    }

}
