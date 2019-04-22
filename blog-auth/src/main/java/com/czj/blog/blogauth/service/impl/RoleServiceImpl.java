package com.czj.blog.blogauth.service.impl;

import com.alibaba.fastjson.JSON;
import com.czj.blog.blogauth.dao.RoleDao;
import com.czj.blog.blogauth.domain.Right;
import com.czj.blog.blogauth.domain.Role;
import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogauth.service.RoleService;
import com.czj.blog.blogauth.utils.DateUtil;
import com.czj.blog.blogauth.utils.SnowflakeIdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        String id = SnowflakeIdWorker.generateId();
        role.setId(id);
        role.setCreateTime(DateUtil.getCurrentTime());
        role.setEnable("1");
        return roleDao.insertRole(role);
    }

    @Override
    public Integer updateRole(Role role) {
        role.setUpdateTime(DateUtil.getCurrentTime());
        return roleDao.updateRole(role);
    }


    @Override
    public Integer deleteRoles(List<String> ids) {
        Integer integer = roleDao.deleteRoles(ids);
        Integer integer1 = roleDao.deleteRoleRightById(ids);
        return integer;
    }

    @Override
    public Integer deleteRoleRightByDoubleId(String roleId, String rightId) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("rightId", rightId);
        hashMap.put("roleId", roleId);
        Integer integer = roleDao.deleteRightRoleByDoubleId(hashMap);
        return integer;
    }

    @Override
    public PageInfo selectOtherRights(List<String> ids, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Right> rights = roleDao.selectOtherRights(ids);
        if (CollectionUtils.isEmpty(rights)) {
            PageInfo<Right> pageInfo = new PageInfo<>(Lists.newArrayList());
            return pageInfo;
        } else {
            PageInfo<Right> pageInfo = new PageInfo<>(rights);
            return pageInfo;
        }
    }

    @Override
    public Map<String, Object> insertRoleRight(String roleId, List<String> rightIds) {
        Map<String, String> map = new HashMap<>();
        Map<String, Object> returnMap = new HashMap<>();
        map.put("roleId",roleId);
        Integer sum=0;
        for (String rightId:rightIds) {
            String id = SnowflakeIdWorker.generateId();
            map.put("id",id);
            map.put("rightId",rightId);
            Integer integer = roleDao.insertRoleRight(map);
            sum+=integer;
        } if (sum==rightIds.size()){
            List<Right> rights = roleDao.selectRights(roleId);
            returnMap.put("rights",rights);
        }
        returnMap.put("integer",sum);
        return returnMap;
    }
}
