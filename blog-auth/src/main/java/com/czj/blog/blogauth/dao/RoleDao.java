package com.czj.blog.blogauth.dao;


import com.czj.blog.blogauth.domain.Role;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-01-29 15:24
 */

public interface RoleDao {
    public Role selectRoleById(Long id);
    public Integer insertRole(Role role);
    public Integer updateRole(Role role);
    public Integer deleteRole(Long id);
    public Integer deleteRoles(List<Long> ids);
}
