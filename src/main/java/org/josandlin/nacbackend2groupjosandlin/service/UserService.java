package org.josandlin.nacbackend2groupjosandlin.service;

import org.josandlin.nacbackend2groupjosandlin.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);


    /*
    @Transactional
    UserDTO createUser(UserDTO userDTO);

    @Transactional
    UserDTO deleteUser(UserDTO userDTO);

    @Transactional
    UserDTO editUser(UserDTO userDTO);


 */
}
