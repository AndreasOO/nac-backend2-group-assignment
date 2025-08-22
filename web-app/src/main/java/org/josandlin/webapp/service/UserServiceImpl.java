package org.josandlin.webapp.service;


import org.josandlin.webapp.dao.UserDao;
import org.josandlin.library.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.josandlin.library.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(final UserDao userDao, final UserMapper userMapper) {
        this.userDao = userDao;
        this.userMapper = userMapper;
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
}
