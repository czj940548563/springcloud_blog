package com.czj.blog.blogauth.service;



import com.czj.blog.blogauth.domain.User;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-01-30 14:11
 */
public interface UserService {
    public User selectUser(User user);
    public List<User> selectAllUser();
    public String insertUser(User user);
    public Integer updateUser(User user);
    public Integer updateLoginTime(User user);
    public Integer deleteUser(Long id);
    public Integer deleteUsers(List<Long> ids);
}
