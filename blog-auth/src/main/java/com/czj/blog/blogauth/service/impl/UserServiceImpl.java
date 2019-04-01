package com.czj.blog.blogauth.service.impl;



import com.czj.blog.blogauth.dao.UserDao;
import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogauth.service.UserService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-01-30 15:06
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User selectUser(User user) {
        return userDao.selectUser(user);
    }

    @Override
    public List<User> selectAllUser() {
        if (CollectionUtils.isEmpty(userDao.selectAllUser())){
            return Lists.newArrayList();
        }
        return userDao.selectAllUser();
    }

    @Override
    public Integer insertUser(User user) {

        return userDao.insertUser(user);
    }

    @Override
    public Integer updateUser(User user) {
        return null;
    }

    @Override
    public Integer updateLoginTime(User user) {
        return null;
    }

    @Override
    public Integer deleteUser(Long id) {
        return null;
    }

    @Override
    public Integer deleteUsers(List<Long> ids) {
        return null;
    }
}
