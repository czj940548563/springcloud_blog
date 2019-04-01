package com.czj.blog.blogfeign.service;

import com.czj.blog.blogauth.domain.Role;
import com.czj.blog.blogauth.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-03-27 17:20
 */
@FeignClient(value = "blog-auth")
public interface SchedualBlogAuth {
    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);

    @RequestMapping(value = "/selectAllUser", method = RequestMethod.GET)
    List<User> selectAllUser();

    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    Long insertUser(@RequestBody User user);

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    Integer deleteUser(@RequestParam(value = "id") long id);

    @RequestMapping(value = "/deleteUsers", method = RequestMethod.POST)
    Integer deleteUsers(@RequestParam(value = "ids") List<Long> ids);

    @PostMapping(value = "/updateUser")
    Integer updateUser(@RequestBody User user);

    @PostMapping("selectUserByAccount")
    User selectUserByAccount(@RequestParam(value = "account") String account);

    @PostMapping("/selectRoleByName")
    Role selectRoleByName(@RequestParam(value = "name") String name);

    @PostMapping(value = "/insertRole")
    Long insertRole(@RequestBody Role role);

    @PostMapping(value = "/updateRole")
    Integer updateRole(@RequestBody Role role);

    @PostMapping(value = "/deleteRole")
    Integer deleteRole(@RequestParam(value = "id") Long id);

    @PostMapping(value = "/deleteRoles")
    Integer deleteRoles(@RequestParam(value = "ids") List<Long> ids);
}
