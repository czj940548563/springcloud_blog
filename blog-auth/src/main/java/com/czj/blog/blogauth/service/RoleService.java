package com.czj.blog.blogauth.service;

import com.czj.blog.blogauth.domain.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author: clownc
 * @Date: 2019-04-01 15:41
 */
public interface RoleService {
    Role selectRoleByName(String name);

    PageInfo selectAllRole(int pageNum, int pageSize);

    Integer insertRole(Role role);

    Integer updateRole(Role role);

    Integer deleteRoles(List<String> ids);

    Integer deleteRoleRightByDoubleId(String roleId, String rightId);

    PageInfo selectOtherRights(List<String> ids, int pageNum, int pageSize);

    Map<String,Object> insertRoleRight(String roleId, List<String> rightIds);

    List<String> selectUserIdByRoleId(List<String> ids);

}
