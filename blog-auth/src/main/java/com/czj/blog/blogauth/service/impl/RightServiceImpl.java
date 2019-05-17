package com.czj.blog.blogauth.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.czj.blog.blogauth.RedisUtils.RedisUtils;
import com.czj.blog.blogauth.dao.RightDao;
import com.czj.blog.blogauth.dao.RoleDao;
import com.czj.blog.blogauth.domain.Right;
import com.czj.blog.blogauth.domain.Role;
import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogauth.service.RightService;
import com.czj.blog.blogauth.utils.DateUtil;
import com.czj.blog.blogauth.utils.SnowflakeIdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: clownc
 * @Date: 2019-04-17 9:44
 */
@Service
public class RightServiceImpl implements RightService {
    @Autowired
    private RightDao rightDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RedisUtils redisUtils;
    private String userStr = "user";

    @Override
    public Right selectRight(Right right) {
        return null;
    }

    @Override
    public PageInfo selectAllRight(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Right> rightList = rightDao.selectAllRight();
        if (CollectionUtils.isEmpty(rightList)) {
            PageInfo<Right> pageInfo = new PageInfo<>(Lists.newArrayList());
            return pageInfo;
        } else {
            PageInfo<Right> pageInfo = new PageInfo<>(rightList);
            return pageInfo;
        }
    }

    @Override
    public Integer insertRight(Right right) {
        String id = SnowflakeIdWorker.generateId();
        right.setId(id);
        right.setCreateTime(DateUtil.getCurrentTime());
        right.setEnable("1");
        return rightDao.insertRight(right);
    }

    @Override
    public Integer updateRight(Right right) {
        right.setUpdateTime(DateUtil.getCurrentTime());
        String id = right.getId();
        List<String> ids = new ArrayList<>();
        ids.add(id);
        List<String> roleIds = rightDao.selectRoleIdByRightId(ids);
        if (roleIds != null && roleIds.size() > 0) {
            List<String> userIds = roleDao.selectUserIdByRoleId(roleIds);
            /**
             * 跟缓存中的用户权限保持一致性
             */
            if (userIds != null && userIds.size() > 0) {
                for (String userId : userIds) {
                    Set<String> strings = redisUtils.zRangeByScore(userStr, Double.parseDouble(userId), Double.parseDouble(userId));
                    if (strings != null && strings.size() > 0) {
                        List<User> users = UserServiceImpl.getCacheBean(strings);
                        User user = users.get(0);
                        List<Role> roles = user.getRoles();
                        for (Role role : roles) {
                            List<Right> rights = role.getRights();
                            Integer i = 0;
                            for (Right right1 : rights) {
                                if (StringUtils.equals(id, right1.getId())) break;
                                else i++;
                            }
                            if (rights.size() > i) {
                                rights.set(i, right);
                            }
                        }
                        //消除对同一对象循环引用的问题，默认为false
                        String userJson = JSON.toJSONString(user, SerializerFeature.DisableCircularReferenceDetect);
                        //更新数据，先删除缓存再新增
                        redisUtils.zRemoveRangeByScore(userStr, Double.parseDouble(userId), Double.parseDouble(userId));
                        redisUtils.zAdd(userStr, userJson, Double.parseDouble(userId));
                    }

                }
            }
        }
        return rightDao.updateRight(right);
    }

    @Override
    public Integer deleteRights(List<String> ids) {
        List<String> list = rightDao.selectRoleIdByRightId(ids);
        if (list.size() > 0) return -1;
        Integer integer = rightDao.deleteRights(ids);
        return integer;
    }

    @Override
    public List<String> selectRoleIdByRightId(List<String> ids) {
        return rightDao.selectRoleIdByRightId(ids);
    }

    @Override
    public Right selectRightByName(String name) {
        return rightDao.selectRightByName(name);
    }
}
