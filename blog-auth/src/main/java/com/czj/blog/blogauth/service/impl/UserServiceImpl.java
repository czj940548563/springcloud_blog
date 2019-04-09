package com.czj.blog.blogauth.service.impl;


import com.czj.blog.blogauth.dao.RoleDao;
import com.czj.blog.blogauth.dao.UserDao;
import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogauth.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public PageInfo selectAllUser(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userDao.selectAllUser();
        if (CollectionUtils.isEmpty(userList)){
            PageInfo<User> pageInfo = new PageInfo<>(Lists.newArrayList());
            return pageInfo;
        }else{
            PageInfo<User> pageInfo = new PageInfo<>(userList);
            return  pageInfo;
        }

    }

    @Override
    public Integer insertUser(User user) {

        return userDao.insertUser(user);
    }

    @Override
    public Integer updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public Integer deleteUsers( List<String> ids) {
        Integer integer = userDao.deleteUsers(ids);
        Integer integer1 = userDao.deleteUserRoleById(ids);
        return integer;
    }

    @Override
    public User selectUserByAccount(String account) {
        return userDao.selectUserByAccount(account);
    }

    @Override
    public Integer deleteUserRoleByDoubleId(String userId,String roleId) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId",userId);
        hashMap.put("roleId",roleId);
        Integer integer = userDao.deleteUserRoleByDoubleId(hashMap);
        return integer;
    }
}
