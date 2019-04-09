package com.czj.blog.blogauth.service.impl;

import com.czj.blog.blogauth.dao.RoleDao;
import com.czj.blog.blogauth.domain.Role;
import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogauth.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-04-01 15:41
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Override
    public Role selectRoleByName(String name) {
        return roleDao.selectRoleByName(name);
    }

    @Override
    public PageInfo selectAllRole(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Role> roleList = roleDao.selectAllRole();
        if (CollectionUtils.isEmpty(roleList)){
            PageInfo<Role> pageInfo = new PageInfo<>(Lists.newArrayList());
            return pageInfo;
        }else{
            PageInfo<Role> pageInfo = new PageInfo<>( roleList);
            return  pageInfo;
        }
    }

    @Override
    public Integer insertRole(Role role) {
        return roleDao.insertRole(role);
    }

    @Override
    public Integer updateRole(Role role) {
        return roleDao.updateRole(role);
    }


    @Override
    public Integer deleteRoles(List<String> ids) {
        Integer integer1 = roleDao.deleteUserRoleById(ids);
        return roleDao.deleteRoles(ids);
    }
}
