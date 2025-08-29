package org.josandlin.webapp.service;

import org.josandlin.library.dto.UserCreateDTO;
import org.josandlin.library.dto.UserDTO;
import org.josandlin.webapp.utils.ResultMessage;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    ResultMessage createUser(UserCreateDTO userDTO);

}
