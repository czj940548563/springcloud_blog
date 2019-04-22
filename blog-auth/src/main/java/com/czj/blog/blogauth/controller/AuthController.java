package com.czj.blog.blogauth.controller;

import com.czj.blog.blogauth.domain.Role;
import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogauth.service.RoleService;
import com.czj.blog.blogauth.service.UserService;
import com.czj.blog.blogauth.utils.SnowflakeIdWorker;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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


    @RequestMapping(value = "/selectAllUser", method = RequestMethod.GET)
    public PageInfo selectAllUser(@RequestParam int pageNum, @RequestParam int pageSize) {
        return userService.selectAllUser(pageNum, pageSize);
    }

    @PostMapping("/selectOtherRoles")
    public PageInfo selectOtherRoles(@RequestParam(required = false) List<String> ids, @RequestParam int pageNum, @RequestParam int pageSize) {
        return userService.selectOtherRoles(ids, pageNum, pageSize);
    }

    @PostMapping("/selectOtherRights")
    public PageInfo selectOtherRights(@RequestParam(required = false) List<String> ids, @RequestParam int pageNum, @RequestParam int pageSize) {
        return roleService.selectOtherRights(ids, pageNum, pageSize);
    }

    @PostMapping(value = "/insertUser")
    public Integer insertUser(@RequestBody User user) {

        Integer integer = userService.insertUser(user);
         return integer;
    }

    @PostMapping(value = "/deleteUsers")
    public Integer deleteUsers(@RequestParam List<String> ids) {
        return userService.deleteUsers(ids);
    }

    @PostMapping(value = "/insertUserRole")
    public Map<String, Object> insertUserRole(@RequestParam List<String> roleIds, @RequestParam String userId) {

        return userService.insertUserRole(userId, roleIds);
    }

    @PostMapping(value = "/insertRoleRight")
    public Map<String, Object> insertRoleRight(@RequestParam List<String> rightIds, @RequestParam String roleId) {

        return roleService.insertRoleRight(roleId, rightIds);
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
    public Role selectRoleByName(@RequestParam String name) {
        return roleService.selectRoleByName(name);
    }

    @PostMapping(value = "/insertRole")
    public Integer insertRole(@RequestBody Role role) {
        Integer integer = roleService.insertRole(role);
        return integer;
    }

    @PostMapping(value = "/updateRole")
    public Integer updateRole(@RequestBody Role role) {
        return roleService.updateRole(role);
    }

    @PostMapping(value = "/deleteRoles")
    public Integer deleteRoles(@RequestParam List<String> ids) {
        return roleService.deleteRoles(ids);
    }

    @PostMapping(value = "/deleteRoleByUser")
    public Integer deleteRoleByUser(@RequestParam String userId, @RequestParam String roleId) {
        return userService.deleteUserRoleByDoubleId(userId, roleId);
    }

    @RequestMapping(value = "/selectAllRole", method = RequestMethod.GET)
    public PageInfo selectAllRole(@RequestParam int pageNum, @RequestParam int pageSize) {
        return roleService.selectAllRole(pageNum, pageSize);
    }

    @PostMapping(value = "/deleteRightByRole")
    public Integer deleteRightByRole(@RequestParam String roleId, @RequestParam String rightId) {
        return roleService.deleteRoleRightByDoubleId(roleId, rightId);
    }
}
