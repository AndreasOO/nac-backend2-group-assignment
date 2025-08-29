package org.josandlin.webapp.service;

import org.josandlin.library.entity.user.Role;
import org.josandlin.webapp.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(final RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role getRoleByName(String name) {
        Optional<Role> role = roleDao.findRoleByName(name);
        return role.orElse(null);
    }
}
