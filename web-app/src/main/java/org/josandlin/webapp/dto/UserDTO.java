package org.josandlin.nacbackend2groupjosandlin.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    Long id;

    String username;

    String password;

    List<RoleDTO> roles = new ArrayList<>();

    public UserDTO() {}

    public UserDTO(Long id, String username, String password, List<RoleDTO> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }
}
