package com.czj.blog.blogfeign.controller;

import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogfeign.result.Result;
import com.czj.blog.blogfeign.service.SchedualBlogAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-03-27 17:23
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    SchedualBlogAuth schedualBlogAuth;
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    public String sayHi(@RequestParam String name){
        return schedualBlogAuth.sayHiFromClientOne(name);
    }
    @RequestMapping(value = "/selectAllUser",method = RequestMethod.GET)
    public Result<List> selectAllUser(){
        List<User> users = schedualBlogAuth.selectAllUser();
        return Result.success(users);
    }

    /**
     * 超时熔断测试
     * @return
     */
    @RequestMapping("/timeout")
    public String timeout(){
        try{
            //睡6秒，网关Hystrix5秒超时，会触发熔断降级操作
            Thread.sleep(6000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "timeout";
    }
}
