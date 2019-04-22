package com.czj.blog.blogauth.service;

import com.czj.blog.blogauth.domain.Role;
import com.czj.blog.blogauth.domain.User;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author: clownc
 * @Date: 2019-01-30 14:11
 */
public interface UserService {
    User selectUser(User user);
    PageInfo selectAllUser(int pageNum, int pageSize);
    Integer insertUser(User user);
    Integer updateUser(User user);
    Integer deleteUsers(List<String> ids);
    User selectUserByAccount(String account);
    Integer deleteUserRoleByDoubleId(String userId,String roleId );
    PageInfo selectOtherRoles(List<String> ids,int pageNum, int pageSize);
    Map<String,Object> insertUserRole( String userId, List<String> roleIds);
}
