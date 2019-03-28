package com.czj.blog.blogauth.dao;

import com.czj.blog.blogauth.domain.User;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-01-29 15:22
 */
public interface UserDao {
    User selectUser(User user);

    Integer insertUser(User user);

    Integer updateUser(User user);

    Integer updateLoginTime(User user);

    Integer deleteUser(Long id);

    Integer deleteUsers(List<Long> ids);

    List<User> selectAllUser();
}
