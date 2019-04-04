package com.czj.blog.blogfeign.controller;

import com.czj.blog.blogauth.domain.Role;
import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogauth.utils.SnowflakeIdWorker;
import com.czj.blog.blogfeign.result.CodeMsg;
import com.czj.blog.blogfeign.result.Result;
import com.czj.blog.blogfeign.service.SchedualBlogAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    public String sayHi(@RequestParam String name) {
        return schedualBlogAuth.sayHiFromClientOne(name);
    }

    @RequestMapping(value = "/selectAllUser", method = RequestMethod.GET)
    public Result<List> selectAllUser() {
        List<User> users = schedualBlogAuth.selectAllUser();
        return Result.success(users);
    }

    @PostMapping(value = "/regist")
    public Result<User> regist(@RequestBody User user) {
        String account = user.getAccount();
        User userExits = schedualBlogAuth.selectUserByAccount(account);
        if (userExits != null) return Result.error(CodeMsg.USER_EXITS);
        else {
            Long id = schedualBlogAuth.insertUser(user);
            if (id != 0) {
                user.setId(id);
                return Result.success(user);
            } else return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public Result<User> deleteUser(@RequestParam(value = "id") long id) {
        Integer integer = schedualBlogAuth.deleteUser(id);
        if (integer > 0) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping(value = "/deleteUsers", method = RequestMethod.POST)
    public Result<User> deleteUsers(@RequestParam(value = "ids") List<Long> ids) {
        Integer integer = schedualBlogAuth.deleteUsers(ids);
        if (integer > 0 && ids.size() == integer) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }

    @PostMapping(value = "/updateUser")
    public Result<User> updateUser(@RequestBody User user) {
        Integer integer = schedualBlogAuth.updateUser(user);
        if (integer > 0) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }

    @PostMapping("/selectUserByAccount")
    public Result<User> selectUserByAccount(@RequestParam(value = "account") String account) {
        User user = schedualBlogAuth.selectUserByAccount(account);
        if (user != null) return Result.success(user);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }

    @PostMapping("/selectRoleByName")
    public Result<Role> selectRoleByName(@RequestParam(value = "name") String name){
        Role role = schedualBlogAuth.selectRoleByName(name);
        if (role != null) return Result.success(role);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }
    @PostMapping(value = "/insertRole")
    public Result<Role> insertRole(@RequestBody Role role){
        String name = role.getRoleName();
        Role roleExits = schedualBlogAuth.selectRoleByName(name);
        if (roleExits != null) return Result.error(CodeMsg.ROLE_EXITS);
        else {
            Long id = schedualBlogAuth.insertRole(role);
            if (id != 0) {
                role.setId(id);
                return Result.success(role);
            } else return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
    @PostMapping(value = "/updateRole")
    public Result<Object> updateRole(@RequestBody Role role){
        Integer integer = schedualBlogAuth.updateRole(role);
        if (integer > 0) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }
    @PostMapping(value = "/deleteRole")
    public Result<Object> deleteRole(@RequestParam(value = "id") Long id){
        Integer integer = schedualBlogAuth.deleteRole(id);
        if (integer > 0) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }
    @PostMapping(value = "/deleteRoles")
    public Result<Object> deleteRoles(@RequestParam(value = "ids") List<Long> ids){
        Integer integer = schedualBlogAuth.deleteRoles(ids);
        if (integer > 0 && ids.size() == integer) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }
    @PostMapping(value = "/deleteRoleByUser")
    public Result<Object> deleteRoleByUser(@RequestParam(value = "userId",required=false)String userId ,@RequestParam(value = "roleId",required=false) String roleId){
        Integer integer = schedualBlogAuth.deleteRoleByUser(Long.parseLong(userId),Long.parseLong(roleId));
        if (integer > 0 ) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }

    /**
     * 超时熔断测试
     *
     * @return
     */
    @RequestMapping("/timeout")
    public String timeout() {
        try {
            //睡6秒，网关Hystrix5秒超时，会触发熔断降级操作
            Thread.sleep(6000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "timeout";
    }
}
