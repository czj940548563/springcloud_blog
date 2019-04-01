package com.czj.blog.blogfeign.service;

import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogauth.utils.SnowflakeIdWorker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-03-27 17:20
 */
@FeignClient(value = "blog-auth")
public interface SchedualBlogAuth {
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
    @RequestMapping(value = "/selectAllUser",method = RequestMethod.GET)
    List<User> selectAllUser();
    @PostMapping(value = "/regist")
     Integer insertUser(User user) ;

}
