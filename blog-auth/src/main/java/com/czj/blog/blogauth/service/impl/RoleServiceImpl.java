package com.czj.blog.blogauth.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.czj.blog.blogauth.RedisUtils.RedisUtils;
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
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author: clownc
 * @Date: 2019-04-01 15:41
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RedisUtils redisUtils;
    private String userStr = "user";

    @Override
    public Role selectRoleByName(String name) {
        return roleDao.selectRoleByName(name);
    }

    @Override
    public PageInfo selectAllRole(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Role> roleList = roleDao.selectAllRole();
        if (CollectionUtils.isEmpty(roleList)) {
            PageInfo<Role> pageInfo = new PageInfo<>(Lists.newArrayList());
            return pageInfo;
        } else {
            PageInfo<Role> pageInfo = new PageInfo<>(roleList);
            return pageInfo;
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
        String id = role.getId();
        List<String> ids = new ArrayList<>();
        ids.add(id);
        List<String> userIds = roleDao.selectUserIdByRoleId(ids);
        /**
         * 跟缓存中的用户角色保持一致性
         */
        if (userIds != null && userIds.size() > 0) {
            for (String userId : userIds) {
                Set<String> strings = redisUtils.zRangeByScore(userStr, Double.parseDouble(userId), Double.parseDouble(userId));
                if (strings != null && strings.size() > 0) {
                    List<User> users = UserServiceImpl.getCacheBean(strings);
                    User user = users.get(0);
                    List<Role> roles = user.getRoles();
                    Integer i = 0;
                    for (Role role1 : roles) {
                        if (StringUtils.equals(role1.getId(), id)) break;
                        else i++;
                    }
                    roles.set(i, role);
                    String userJson = JSON.toJSONString(user);
                    //更新数据，先删除缓存再新增
                    redisUtils.zRemoveRangeByScore(userStr, Double.parseDouble(userId), Double.parseDouble(userId));
                    redisUtils.zAdd(userStr, userJson, Double.parseDouble(userId));
                }

            }
        }
        return roleDao.updateRole(role);
    }


    @Override
    public Integer deleteRoles(List<String> ids) {
        List<String> list = roleDao.selectUserIdByRoleId(ids);
        if (list != null && list.size() > 0) return -1;
        Integer integer = roleDao.deleteRoles(ids);
        if (integer == ids.size()) {
            Integer integer1 = roleDao.deleteRoleRightById(ids);
        }
        return integer;
    }

    @Override
    public Integer deleteRoleRightByDoubleId(String roleId, String rightId) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("rightId", rightId);
        hashMap.put("roleId", roleId);
        Integer integer = roleDao.deleteRightRoleByDoubleId(hashMap);
        if (integer > 0) {
            List<String> roleIds = new ArrayList<>();
            roleIds.add(roleId);
            List<String> userIds = roleDao.selectUserIdByRoleId(roleIds);
            if (userIds != null && userIds.size() > 0) {
                for (String userId : userIds) {
                    Set<String> strings = redisUtils.zRangeByScore(userStr, Double.parseDouble(userId), Double.parseDouble(userId));
                    if (strings != null && strings.size() > 0) {
                        List<User> users = UserServiceImpl.getCacheBean(strings);
                        User user = users.get(0);
                        List<Role> roles = user.getRoles();
                        for (Role role1 : roles) {
                            List<Right> rights = role1.getRights();
                            int i = 0;//如果用remove不能直接用包装类
                            for (Right right1 : rights) {
                                if (StringUtils.equals(rightId, right1.getId())) break;
                                else i++;
                            }
                            if (StringUtils.equals(role1.getId(), roleId)) {
                                rights.remove(i);
                            }
                        }
                        String userJson = JSON.toJSONString(user, SerializerFeature.DisableCircularReferenceDetect);
                        //更新数据，先删除缓存再新增
                        redisUtils.zRemoveRangeByScore(userStr, Double.parseDouble(userId), Double.parseDouble(userId));
                        redisUtils.zAdd(userStr, userJson, Double.parseDouble(userId));
                    }

                }
            }
        }
        return integer;
    }

    @Override
    public PageInfo selectOtherRights(List<String> ids, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
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
        map.put("roleId", roleId);
        Integer sum = 0;
        /**
         * 关联关系插入中间表
         */
        for (String rightId : rightIds) {
            String id = SnowflakeIdWorker.generateId();
            map.put("id", id);
            map.put("rightId", rightId);
            Integer integer = roleDao.insertRoleRight(map);
            sum += integer;
        }
        if (sum == rightIds.size()) {
            List<String> roleIds = new ArrayList<>();
            List<Right> rights = roleDao.selectRights(roleId);
            roleIds.add(roleId);
            List<String> userIds = roleDao.selectUserIdByRoleId(roleIds);
            if (userIds != null && userIds.size() > 0) {
                for (String userId : userIds) {
                    Set<String> strings = redisUtils.zRangeByScore(userStr, Double.parseDouble(userId), Double.parseDouble(userId));
                    if (strings != null && strings.size() > 0) {
                        List<User> users = UserServiceImpl.getCacheBean(strings);
                        User user = users.get(0);
                        List<Role> roles = user.getRoles();
                        Integer i = 0;
                        for (Role role1 : roles) {
                            if (StringUtils.equals(role1.getId(), roleId))
                                role1.setRights(rights);
                        }
                        String userJson = JSON.toJSONString(user);
                        //更新数据，先删除缓存再新增
                        redisUtils.zRemoveRangeByScore(userStr, Double.parseDouble(userId), Double.parseDouble(userId));
                        redisUtils.zAdd(userStr, userJson, Double.parseDouble(userId));
                    }

                }
            }
            returnMap.put("rights", rights);
        }
        returnMap.put("integer", sum);
        return returnMap;
    }

    @Override
    public List<String> selectUserIdByRoleId(List<String> ids) {
        return roleDao.selectUserIdByRoleId(ids);
    }


}
