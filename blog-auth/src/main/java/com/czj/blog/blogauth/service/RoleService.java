package com.czj.blog.blogauth.service;

import com.czj.blog.blogauth.domain.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-04-01 15:41
 */
public interface RoleService {
    public Role selectRoleByName(String name);
    public PageInfo selectAllRole(int pageNum, int pageSize);
    public Integer insertRole(Role role);
    public Integer updateRole(Role role);
    public Integer deleteRoles(List<String> ids);
}
