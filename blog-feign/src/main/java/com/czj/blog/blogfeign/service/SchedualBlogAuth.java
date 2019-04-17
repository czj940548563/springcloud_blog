package com.czj.blog.blogfeign.service;

import com.czj.blog.blogauth.domain.Role;
import com.czj.blog.blogauth.domain.User;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: clownc
 * @Date: 2019-03-27 17:20
 */
@FeignClient(value = "blog-auth")
public interface SchedualBlogAuth {
    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);

    @RequestMapping(value = "/selectAllUser", method = RequestMethod.GET)
    PageInfo selectAllUser(@RequestParam(value = "pageNum") int pageNum,@RequestParam(value = "pageSize") int pageSize);

    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    String insertUser(@RequestBody User user);
    @PostMapping("/insertUserRole")
    Map<String,Object> insertUserRole(@RequestParam(value = "roleIds") List<String> roleIds, @RequestParam(value = "userId") String userId);
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    Integer deleteUser(@RequestParam(value = "id") String id);

    @RequestMapping(value = "/deleteUsers", method = RequestMethod.POST)
    Integer deleteUsers(@RequestParam(value = "ids") List<String> ids);

    @PostMapping(value = "/updateUser")
    Integer updateUser(@RequestBody User user);

    @PostMapping("selectUserByAccount")
    User selectUserByAccount(@RequestParam(value = "account") String account);

    @PostMapping("selectOtherRoles")
    PageInfo selectOtherRoles(@RequestParam(value = "ids") List<String> ids,@RequestParam(value = "pageNum") int pageNum,@RequestParam(value = "pageSize") int pageSize);

    @PostMapping("/selectRoleByName")
    Role selectRoleByName(@RequestParam(value = "name") String name);

    @PostMapping(value = "/insertRole")
    String insertRole(@RequestBody Role role);

    @PostMapping(value = "/updateRole")
    Integer updateRole(@RequestBody Role role);

    @PostMapping(value = "/deleteRole")
    Integer deleteRole(@RequestParam(value = "id") String id);

    @PostMapping(value = "/deleteRoles")
    Integer deleteRoles(@RequestParam(value = "ids") List<String> ids);

    @PostMapping(value = "/deleteRoleByUser")
    Integer deleteRoleByUser(@RequestParam(value = "userId")String userId,@RequestParam(value = "roleId")String roleId);
    @RequestMapping(value = "/selectAllRole", method = RequestMethod.GET)
    PageInfo selectAllRole(@RequestParam(value = "pageNum") int pageNum,@RequestParam(value = "pageSize") int pageSize);
}
