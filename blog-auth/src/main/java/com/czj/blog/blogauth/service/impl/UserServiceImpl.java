package com.czj.blog.blogauth.service.impl;


import com.alibaba.fastjson.JSON;
import com.czj.blog.blogauth.RedisUtils.RedisUtils;
import com.czj.blog.blogauth.dao.RightDao;
import com.czj.blog.blogauth.dao.RoleDao;
import com.czj.blog.blogauth.dao.UserDao;
import com.czj.blog.blogauth.domain.Right;
import com.czj.blog.blogauth.domain.Role;
import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogauth.service.UserService;
import com.czj.blog.blogauth.utils.DateUtil;
import com.czj.blog.blogauth.utils.SnowflakeIdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: clownc
 * @Date: 2019-01-30 15:06
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RightDao rightDao;
    @Autowired
    private RedisUtils redisUtils;

    private String userStr = "user";


    @Override
    public User selectUser(User user) {
        return userDao.selectUser(user);
    }


    public static List<User> getCacheBean(Set<String> strings) {
        List<User> users = new ArrayList<>();
        for (String s : strings) {
            User user = JSON.parseObject(s, User.class);
            users.add(user);
        }
        return users;
    }

    @Override
    public PageInfo selectAllUser(int pageNum, int pageSize) {
        Long start = (pageNum - 1L) * pageSize;
        Long end = pageNum * pageSize * 1L - 1L;
        /**
         * 先从缓存里面按分页查找，zset是顺序集合的缓存
         */
        Set<String> strings = redisUtils.zRange(userStr, start, end);
        if (strings != null && strings.size() > 0) {
            Long size = redisUtils.zSize(userStr);
            List<User> users = UserServiceImpl.getCacheBean(strings);
            PageInfo<User> pageInfo = new PageInfo<>(users);
            pageInfo.setPageNum(pageNum);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTotal(size);
            return pageInfo;
        } else {
            /**
             * 缓存中不存在则从数据库中按分页查找，并存入缓存
             * 为防止窜页，先查到所有数据放入缓存
             */
            List<User> userList = userDao.selectAllUser();
            if (CollectionUtils.isEmpty(userList)) {
                PageInfo<User> pageInfo = new PageInfo<>(Lists.newArrayList());
                return pageInfo;
            } else {
                for (User user : userList) {
                    double score = Double.parseDouble(user.getId());
                    String userJson = JSON.toJSONString(user);
                    redisUtils.zAdd(userStr, userJson, score);
                    redisUtils.expire(userStr, 30 * 60 * 60, TimeUnit.SECONDS);
                }
                //从缓存里查
                Long size = redisUtils.zSize(userStr);
                Set<String> strings1 = redisUtils.zRange(userStr, start, end);
                List<User> users = UserServiceImpl.getCacheBean(strings1);
                PageInfo<User> pageInfo = new PageInfo<>(users);
                pageInfo.setPageNum(pageNum);
                pageInfo.setPageSize(pageSize);
                pageInfo.setTotal(size);
                return pageInfo;
            }
        }
    }

    @Override
    @Transactional//开启本地事务
    public Integer insertUser(User user) {
        List<Role> roleList = new ArrayList<>();
        List<String> roleIds = new ArrayList<>();
        String id = SnowflakeIdWorker.generateId();
        user.setId(id);
        user.setLoginCount("0");
        user.setCreateTime(DateUtil.getCurrentTime());
        user.setEnable("1");
        String roleName = "普通用户";
        Role role = roleDao.selectRoleByName(roleName);
        List<Right> rights = roleDao.selectRights(role.getId());
        if (rights != null && rights.size() > 0) {
            role.setRights(rights);
        }
        roleList.add(role);
        user.setRoles(roleList);
        Integer integer = userDao.insertUser(user);
        //
        roleIds.add(role.getId());
        insertUserRole(user.getId(), roleIds);
        if (integer > 0) {
            double score = Double.parseDouble(user.getId());
            String userJson = JSON.toJSONString(user);
            redisUtils.zAdd(userStr, userJson, score);
        }
        return integer;
    }

    @Override
    public Integer updateUser(User user) {
        user.setUpdateTime(DateUtil.getCurrentTime());
        Integer integer = userDao.updateUser(user);
        if (integer > 0) {
            double score = Double.parseDouble(user.getId());
            String userJson = JSON.toJSONString(user);
            //更新数据，先删除缓存再新增
            redisUtils.zRemoveRangeByScore(userStr, score, score);
            redisUtils.zAdd(userStr, userJson, score);
        }
        return integer;
    }

    /**
     * 需要事务（待完善）
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public Integer deleteUsers(List<String> ids) {
        Integer integer = userDao.deleteUsers(ids);
        Integer integer1 = userDao.deleteUserRoleById(ids);
        if (integer == ids.size()&&integer1==ids.size()) {
            for (String s : ids) {
                double score = Double.parseDouble(s);
                redisUtils.zRemoveRangeByScore(userStr, score, score);
            }

        }
        return integer;
    }

    @Override
    public User selectUserByAccount(String account) {
        Set<String> strings = redisUtils.zRange(userStr, 0, redisUtils.zSize(userStr) - 1);
        /**
         * 先从缓存里找
         */
        List<User> users = UserServiceImpl.getCacheBean(strings);
        if (users != null && users.size() > 0) {
            for (User user : users) {
                if (StringUtils.equals(account, user.getAccount()))
                    return user;
            }
        }
        /**
         * 缓存没有则查数据库，若查到则放进缓存
         */
        User user = userDao.selectUserByAccount(account);
        if (user != null) {
            double score = Double.parseDouble(user.getId());
            String userJson = JSON.toJSONString(user);
            redisUtils.zAdd(userStr, userJson, score);
        }
        return user;
    }

    /**
     * 删除用户角色中间表中用户与角色的对应关系
     *
     * @param userId
     * @param roleId
     * @return
     */
    @Override
    public Integer deleteUserRoleByDoubleId(String userId, String roleId) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", userId);
        hashMap.put("roleId", roleId);
        Integer integer = userDao.deleteUserRoleByDoubleId(hashMap);
        if (integer > 0) {
            double score = Double.parseDouble(userId);
            Set<String> strings = redisUtils.zRangeByScore(userStr, score, score);
            if (strings != null && strings.size() > 0) {
                List<User> users = UserServiceImpl.getCacheBean(strings);
                User user = users.get(0);
                List<Role> roles = user.getRoles();
                List<Role> roles1 = new ArrayList<>();
                if (roles.size() > 0) {
                    roles.forEach(role -> {
                        if (!StringUtils.equals(roleId, role.getId()))
                            roles1.add(role);
                    });
                    user.setRoles(roles1);
                    String userJson = JSON.toJSONString(user);
                    redisUtils.zRemoveRangeByScore(userStr, score, score);
                    redisUtils.zAdd(userStr, userJson, score);
                }
            }

        }
        return integer;
    }

    @Override
    public PageInfo selectOtherRoles(List<String> ids, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Role> roles = userDao.selectOtherRoles(ids);
        if (CollectionUtils.isEmpty(roles)) {
            PageInfo<Role> pageInfo = new PageInfo<>(Lists.newArrayList());
            return pageInfo;
        } else {
            PageInfo<Role> pageInfo = new PageInfo<>(roles);
            return pageInfo;
        }
    }

    /**
     * 需要事务
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> insertUserRole(String userId, List<String> roleIds) {
        Map<String, String> map = new HashMap<>();
        Map<String, Object> returnMap = new HashMap<>();
        map.put("userId", userId);
        Integer sum = 0;
        for (String roleId : roleIds) {
            String id = SnowflakeIdWorker.generateId();
            map.put("id", id);
            map.put("roleId", roleId);
            Integer integer = userDao.insertUserRole(map);
            sum += integer;
        }
        if (sum == roleIds.size()) {
            List<Role> roles = userDao.selectRoles(userId);
            returnMap.put("roles", roles);
            double score = Double.parseDouble(userId);
            Set<String> strings = redisUtils.zRangeByScore(userStr, score, score);
            List<User> users = UserServiceImpl.getCacheBean(strings);
            User user = users.get(0);
            user.setRoles(roles);
            String userJson = JSON.toJSONString(user);
            redisUtils.zRemoveRangeByScore(userStr, score, score);
            redisUtils.zAdd(userStr, userJson, score);
        }
        returnMap.put("integer", sum);
        return returnMap;
    }

}
