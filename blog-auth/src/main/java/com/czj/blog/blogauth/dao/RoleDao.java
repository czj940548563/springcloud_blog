package com.czj.blog.blogauth.dao;


import com.czj.blog.blogauth.domain.Right;
import com.czj.blog.blogauth.domain.Role;

import java.util.List;
import java.util.Map;

/**
 * @Author: clownc
 * @Date: 2019-01-29 15:24
 */

public interface RoleDao {
    Role selectRoleByName(String name);
    Integer insertRole(Role role);
    Integer updateRole(Role role);
    Integer deleteRoleRightById(List<String> ids);
    Integer deleteRoles(List<String> ids);
    List<Role> selectAllRole();

    Integer deleteRightRoleByDoubleId(Map<String,Object> hashMap);

    List<Right> selectOtherRights(List<String> ids);

    Integer insertRoleRight(Map<String,String> map);

    List<Right> selectRights(String roleId);
}
