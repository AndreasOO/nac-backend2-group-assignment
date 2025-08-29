package org.josandlin.webapp.service;


import jakarta.transaction.Transactional;
import org.josandlin.library.dto.RoleDTO;
import org.josandlin.library.dto.UserCreateDTO;
import org.josandlin.library.entity.user.Role;
import org.josandlin.library.entity.user.User;
import org.josandlin.library.mapper.user.RoleMapper;
import org.josandlin.webapp.dao.RoleDao;
import org.josandlin.webapp.dao.UserDao;
import org.josandlin.library.dto.UserDTO;
import org.josandlin.webapp.utils.ResultMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.josandlin.library.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;


import javax.management.relation.RoleNotFoundException;
import javax.xml.transform.Result;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;
    private final RoleDao roleDao;
    private final RoleMapper roleMapper;
    private final RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserDao userDao, final UserMapper userMapper, RoleDao roleDao, RoleMapper roleMapper, RoleService roleService) {
        this.userDao = userDao;
        this.userMapper = userMapper;
        this.roleDao = roleDao;
        this.roleMapper = roleMapper;
        this.roleService = roleService;
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
    public ResultMessage createUser(UserCreateDTO userDTO) {
        Role role = roleService.getRoleByName(userDTO.getRole());
        if (role == null) {
            return new ResultMessage(false, "No role found.");
        }

        User user = userMapper.createDtoToEntity(userDTO);
        user.getRoles().add(role);
        userDao.save(user);

        return new ResultMessage(true, "User created.");
    }
}
