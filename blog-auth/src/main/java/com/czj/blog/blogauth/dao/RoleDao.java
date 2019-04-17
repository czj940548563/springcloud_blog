package com.czj.blog.blogauth.dao;


import com.czj.blog.blogauth.domain.Role;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-01-29 15:24
 */

public interface RoleDao {
    Role selectRoleByName(String name);
    Integer insertRole(Role role);
    Integer updateRole(Role role);
    Integer deleteUserRoleById(List<String> ids);
    Integer deleteRoles(List<String> ids);
    List<Role> selectAllRole();
}
