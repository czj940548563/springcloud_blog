package com.czj.blog.blogauth.dao;

import com.czj.blog.blogauth.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @Author: clownc
 * @Date: 2019-01-29 15:22
 */
public interface UserDao {
    User selectUser(User user);

    Integer insertUser(User user);

    Integer updateUser(User user);


    Integer deleteUsers(List<String> ids);

    List<User> selectAllUser();

    User selectUserByAccount(String account);

    Integer deleteUserRoleById(List<String> ids);

    Integer deleteUserRoleByDoubleId(Map<String, Object> params);
}
