package com.czj.blog.blogauth.controller;

import com.czj.blog.blogauth.domain.Role;
import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogauth.service.RoleService;
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
    @Autowired
    private RoleService roleService;

    @RequestMapping("/hi")
    public String home(@RequestParam(value = "name", defaultValue = "forezp") String name) {
        return "hi " + name + " ,i am from port:" + port;
    }

    @RequestMapping(value = "/selectAllUser", method = RequestMethod.GET)
    public List<User> selectAllUser() {
        return userService.selectAllUser();
    }

    @PostMapping(value = "/regist")
    public Long regist(@RequestBody User user) {
        Long id = SnowflakeIdWorker.generateId();
        user.setId(id);
        user.setLoginCount(0);
        if (userService.insertUser(user) > 0) return id;
        else return 0L;
    }

    @PostMapping(value = "/deleteUsers")
    public Integer deleteUsers(@RequestParam List<Long> ids) {
        return userService.deleteUsers(ids);
    }

    @PostMapping(value = "/updateUser")
    public Integer updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PostMapping("/selectUserByAccount")
    public User selectUserByAccount(@RequestParam String account) {
        return userService.selectUserByAccount(account);
    }
    @PostMapping("/selectRoleByName")
    public Role selectRoleByName(@RequestParam String name){
        return roleService.selectRoleByName(name);
    }
    @PostMapping(value = "/insertRole")
    public Long insertRole(@RequestBody Role role){
        Long id = SnowflakeIdWorker.generateId();
        role.setId(id);
        if (roleService.insertRole(role) > 0) return id;
        else return 0L;
    }
    @PostMapping(value = "/updateRole")
    public Integer updateRole(@RequestBody Role role){
        return roleService.updateRole(role);
    }
    @PostMapping(value = "/deleteRoles")
    public Integer deleteRoles(@RequestParam List<Long> ids){
        return roleService.deleteRoles(ids);
    }
}
