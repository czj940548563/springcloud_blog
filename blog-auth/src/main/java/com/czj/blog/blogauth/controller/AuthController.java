package com.czj.blog.blogauth.controller;

import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogauth.service.UserService;
import com.czj.blog.blogauth.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-03-27 16:38
 */
@RestController
public class AuthController {
    @Value("${server.port}")
    String port;

    @Autowired
    private UserService userService;

    @RequestMapping("/hi")
    public String home(@RequestParam(value = "name", defaultValue = "forezp") String name) {
        return "hi " + name + " ,i am from port:" + port;
    }
    @RequestMapping(value = "/selectAllUser",method = RequestMethod.GET)
    public List<User> selectAllUser() {
        return userService.selectAllUser();
    }
    @PostMapping(value = "/regist")
    public Integer regist(User user) {
        Long id = SnowflakeIdWorker.generateId();
        user.setId(id);
        return userService.insertUser(user);
    }
}
