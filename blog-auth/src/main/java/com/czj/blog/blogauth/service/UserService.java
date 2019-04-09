package com.czj.blog.blogauth.service;



import com.czj.blog.blogauth.domain.User;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author: clownc
 * @Date: 2019-01-30 14:11
 */
public interface UserService {
    public User selectUser(User user);
    public PageInfo selectAllUser(int pageNum,int pageSize);
    public Integer insertUser(User user);
    public Integer updateUser(User user);
    public Integer deleteUsers(List<String> ids);
    public User selectUserByAccount(String account);
    public Integer deleteUserRoleByDoubleId(String userId,String roleId );
}
