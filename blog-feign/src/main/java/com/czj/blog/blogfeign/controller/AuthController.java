package com.czj.blog.blogfeign.controller;

import com.czj.blog.blogfeign.service.SchedualBlogAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: clownc
 * @Date: 2019-03-27 17:23
 */
@RestController
public class AuthController {
    @Autowired
    SchedualBlogAuth schedualBlogAuth;
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    public String sayHi(@RequestParam String name){
        return schedualBlogAuth.sayHiFromClientOne(name);
    }
}
