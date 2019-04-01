package com.czj.blog.blogauth.service.impl;

import com.czj.blog.blogauth.dao.RoleDao;
import com.czj.blog.blogauth.domain.Role;
import com.czj.blog.blogauth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-04-01 15:41
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Override
    public Role selectRoleByName(String name) {
        return roleDao.selectRoleByName(name);
    }

    @Override
    public Integer insertRole(Role role) {
        return roleDao.insertRole(role);
    }

    @Override
    public Integer updateRole(Role role) {
        return roleDao.updateRole(role);
    }


    @Override
    public Integer deleteRoles(List<Long> ids) {
        return roleDao.deleteRoles(ids);
    }
}
