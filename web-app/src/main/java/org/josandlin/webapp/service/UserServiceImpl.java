package org.josandlin.webapp.service;


import jakarta.transaction.Transactional;
import org.josandlin.library.dto.RoleDTO;
import org.josandlin.library.entity.user.Role;
import org.josandlin.library.entity.user.User;
import org.josandlin.library.mapper.user.RoleMapper;
import org.josandlin.webapp.dao.RoleDao;
import org.josandlin.webapp.dao.UserDao;
import org.josandlin.library.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.josandlin.library.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;


import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;
    private final RoleDao roleDao;
    private final RoleMapper roleMapper;

    @Autowired
    public UserServiceImpl(final UserDao userDao, final UserMapper userMapper, RoleDao roleDao, RoleMapper roleMapper) {
        this.userDao = userDao;
        this.userMapper = userMapper;
        this.roleDao = roleDao;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userDao.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userDao.findById(id).map(userMapper::toUserDto).orElse(null);
    }

    @Transactional
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        List<Role> roles = userDTO.getRoles()
                .stream()
                .map(roleDTO -> roleDao.findRolesByName(roleDTO.getName())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleDTO.getName())))
                .toList();

        user.getRoles().addAll(roles);
        userDao.save(user);
        System.out.println("SAVED USER: " + user);
        return userDTO;
    }

}

/*
   private List<Role> mapRoles(List<RoleDTO> roleDTOs) {
        return roleDTOs.stream()
                .map(roleDTO -> {
                    try {
                        return roleDao.findbyName(roleDTO.getName())
                                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleDTO.getName()));
                    } catch (RoleNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

     public UserDTO createUser0(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.getRoles().addAll();

        User savedUser = userDao.save(user);
        return userMapper.toUserDto(savedUser);
    }
 */
