package com.czj.blog.blogauth.utils;

import com.alibaba.fastjson.JSON;
import com.czj.blog.blogauth.RedisUtils.RedisUtils;
import com.czj.blog.blogauth.dao.UserDao;
import com.czj.blog.blogauth.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: clownc
 * @Date: 2019-04-11 16:52
 * 初始化数据，将数据以zSet有序集合的形式存到缓存中，以便分页。
 */
@Component
public class DataInitApplicationRunner implements ApplicationRunner {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisUtils redisUtils;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        initUserData();


    }
    public void initUserData(){
        String userStr="user";
        try {
            List<User> userList = userDao.selectAllUser();
            if (userList!=null&&userList.size()>0){
                redisUtils.zRemoveRange(userStr,0,-1);
            }
            for (User user:userList){
                double score = Double.parseDouble(user.getId());
                redisUtils.zRemoveRangeByScore(userStr,score,score);
                String userJson = JSON.toJSONString(user);
                redisUtils.zAdd(userStr,userJson,score);
                redisUtils.expire(userStr,24*60*60, TimeUnit.SECONDS);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
