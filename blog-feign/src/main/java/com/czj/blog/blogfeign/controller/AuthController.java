package com.czj.blog.blogfeign.controller;

import com.czj.blog.blogauth.domain.Right;
import com.czj.blog.blogauth.domain.Role;
import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogcommon.utils.MD5Util;
import com.czj.blog.blogfeign.result.CodeMsg;
import com.czj.blog.blogfeign.result.Result;
import com.czj.blog.blogfeign.service.SchedualBlogAuth;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: clownc
 * @Date: 2019-03-27 17:23
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    SchedualBlogAuth schedualBlogAuth;

    @Value("${server.port}")
    private String port;

    @RequestMapping(value = "/selectAllUser", method = RequestMethod.GET)
    @RequiresPermissions(logical = Logical.AND, value = {"view"})
    public Result<PageInfo> selectAllUser(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize) {
        PageInfo pageInfo = schedualBlogAuth.selectAllUser(pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @PostMapping("/selectOtherRoles")
    @RequiresPermissions(logical = Logical.AND, value = {"view"})
    public Result<PageInfo> selectOtherRoles(@RequestParam(value = "ids", required = false) List<String> ids, @RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize) {
        PageInfo pageInfo = schedualBlogAuth.selectOtherRoles(ids, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @PostMapping("/selectOtherRights")
    @RequiresPermissions(logical = Logical.AND, value = {"view"})
    public Result<PageInfo> selectOtherRights(@RequestParam(value = "ids", required = false) List<String> ids, @RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize) {
        PageInfo pageInfo = schedualBlogAuth.selectOtherRights(ids, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @PostMapping("/insertUserRole")
    @RequiresPermissions(logical = Logical.AND, value = {"insert"})
    public Result<List<Role>> insertUserRole(@RequestParam(value = "roleIds") List<String> roleIds, @RequestParam(value = "userId") String userId) {
        Map<String, Object> map = schedualBlogAuth.insertUserRole(roleIds, userId);
        Integer integer = (Integer) map.get("integer");
        List<Role> roles = (ArrayList<Role>) map.get("roles");
        if (integer == roleIds.size()) {
            return Result.success(roles);
        } else return Result.error(CodeMsg.SERVER_ERROR);
    }

    @PostMapping("/insertRoleRight")
    @RequiresPermissions(logical = Logical.AND, value = {"insert"})
    public Result<List<Right>> insertRoleRight(@RequestParam(value = "rightIds") List<String> rightIds, @RequestParam(value = "roleId") String roleId) {
        Map<String, Object> map = schedualBlogAuth.insertRoleRight(rightIds, roleId);
        Integer integer = (Integer) map.get("integer");
        List<Right> rights = (ArrayList<Right>) map.get("rights");
        if (integer == rightIds.size()) {
            return Result.success(rights);
        } else return Result.error(CodeMsg.SERVER_ERROR);
    }

    private Result<User> commonInsertUser(User user) throws Exception {
        String account = user.getAccount();
        User userExits = schedualBlogAuth.selectUserByAccount(account);
        if (userExits != null) return Result.error(CodeMsg.USER_EXITS);
        else {
            String password = user.getPassword();
            String md5Password = MD5Util.md5(account, password);
            user.setPassword(md5Password);
            Integer integer = schedualBlogAuth.insertUser(user);
            if (integer == 1) {
                return Result.success(user);
            } else return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

    @PostMapping(value = "/regist")
    public Result<User> regist(@RequestBody User user) throws Exception {
        return commonInsertUser(user);
    }

    @PostMapping(value = "/insertUser")
    @RequiresPermissions(logical = Logical.AND, value = {"insert"})
    public Result<User> insertUser(@RequestBody User user) throws Exception {
        return commonInsertUser(user);
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    @RequiresPermissions(logical = Logical.AND, value = {"delete"})
    public Result<User> deleteUser(@RequestParam(value = "id") String id) {
        Integer integer = schedualBlogAuth.deleteUser(id);
        if (integer > 0) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping(value = "/deleteUsers", method = RequestMethod.POST)
    @RequiresPermissions(logical = Logical.AND, value = {"delete"})
    public Result<User> deleteUsers(@RequestParam(value = "ids") List<String> ids) {
        Integer integer = schedualBlogAuth.deleteUsers(ids);
        if (integer > 0 && ids.size() == integer) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }

    @PostMapping(value = "/updateUser")
    @RequiresPermissions(logical = Logical.AND, value = {"edit"})
    public Result<User> updateUser(@RequestBody User user) {
        Integer integer = schedualBlogAuth.updateUser(user);
        if (integer > 0) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }

    @PostMapping("/selectUserByAccount")
    @RequiresPermissions(logical = Logical.AND, value = {"view"})
    public Result<User> selectUserByAccount(@RequestParam(value = "account") String account) {
        User user = schedualBlogAuth.selectUserByAccount(account);
        if (user != null) return Result.error(CodeMsg.USER_EXITS);
        else return Result.error(CodeMsg.ACCOUNT_NOTEXITS);
    }

    @PostMapping("/selectRoleByName")
    @RequiresPermissions(logical = Logical.AND, value = {"view"})
    public Result<Role> selectRoleByName(@RequestParam(value = "name") String name) {
        Role role = schedualBlogAuth.selectRoleByName(name);
        if (role != null) return Result.success(role);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }

    @PostMapping(value = "/insertRole")
    @RequiresPermissions(logical = Logical.AND, value = {"insert"})
    public Result<Role> insertRole(@RequestBody Role role) {
        String name = role.getRoleName();
        Role roleExits = schedualBlogAuth.selectRoleByName(name);
        if (roleExits != null) return Result.error(CodeMsg.ROLE_EXITS);
        else {
            Integer integer = schedualBlogAuth.insertRole(role);
            if (integer == 1) {
                return Result.success(role);
            } else return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

    @PostMapping(value = "/updateRole")
    @RequiresPermissions(logical = Logical.AND, value = {"edit"})
    public Result<Object> updateRole(@RequestBody Role role) {
        Integer integer = schedualBlogAuth.updateRole(role);
        if (integer > 0) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }

    @PostMapping(value = "/deleteRole")
    @RequiresPermissions(logical = Logical.AND, value = {"delete"})
    public Result<Object> deleteRole(@RequestParam(value = "id") String id) {
        Integer integer = schedualBlogAuth.deleteRole(id);
        if (integer > 0) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }

    @PostMapping(value = "/deleteRoles")
    @RequiresPermissions(logical = Logical.AND, value = {"delete"})
    public Result<Object> deleteRoles(@RequestParam(value = "ids") List<String> ids) {
        Integer integer = schedualBlogAuth.deleteRoles(ids);
        if (integer > 0 && ids.size() == integer) return Result.success(null);
        else if (integer == -1) return Result.error(CodeMsg.ROLE_BOUND);
        else return Result.error(CodeMsg.SERVER_ERROR);

    }

    @PostMapping(value = "/deleteRoleByUser")
    @RequiresPermissions(logical = Logical.AND, value = {"delete"})
    public Result<Object> deleteRoleByUser(@RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "roleId", required = false) String roleId) {
        Integer integer = schedualBlogAuth.deleteRoleByUser(userId, roleId);
        if (integer > 0) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }

    @PostMapping(value = "/deleteRightByRole")
    @RequiresPermissions(logical = Logical.AND, value = {"delete"})
    public Result<Object> deleteRightByRole(@RequestParam(value = "roleId", required = false) String roleId, @RequestParam(value = "rightId", required = false) String rightId) {
        Integer integer = schedualBlogAuth.deleteRightByRole(roleId, rightId);
        if (integer > 0) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping(value = "/selectAllRole", method = RequestMethod.GET)
    @RequiresPermissions(logical = Logical.AND, value = {"view"})
    public Result<PageInfo> selectAllRole(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize) {
        PageInfo pageInfo = schedualBlogAuth.selectAllRole(pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @RequestMapping(value = "/selectAllRight", method = RequestMethod.GET)
    @RequiresPermissions(logical = Logical.AND, value = {"view"})
    public Result<PageInfo> selectAllRight(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize) {
        PageInfo pageInfo = schedualBlogAuth.selectAllRight(pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @PostMapping(value = "/insertRight")
    @RequiresPermissions(logical = Logical.AND, value = {"insert"})
    public Result<Right> insertRight(@RequestBody Right right) {
        String name = right.getRightName();
        Right rightExits = schedualBlogAuth.selectRightByName(name);
        if (rightExits != null) return Result.error(CodeMsg.RIGHT_EXITS);
        else {
            Integer integer = schedualBlogAuth.insertRight(right);
            if (integer == 1) {
                return Result.success(right);
            } else return Result.error(CodeMsg.SERVER_ERROR);
        }
    }


    @PostMapping(value = "/deleteRights")
    @RequiresPermissions(logical = Logical.AND, value = {"delete"})
    public Result<Object> deleteRights(@RequestParam(value = "ids") List<String> ids) {
        Integer integer = schedualBlogAuth.deleteRights(ids);
        if (integer > 0 && ids.size() == integer) return Result.success(null);
        else if (integer == -1) return Result.error(CodeMsg.RIGHT_BOUND);
        else return Result.error(CodeMsg.SERVER_ERROR);

    }

    @PostMapping(value = "/updateRight")
    @RequiresPermissions(logical = Logical.AND, value = {"edit"})
    public Result<Object> updateRight(@RequestBody Right right) {
        Integer integer = schedualBlogAuth.updateRight(right);
        if (integer > 0) return Result.success(null);
        else return Result.error(CodeMsg.SERVER_ERROR);
    }


    /**
     * spring session共享test
     *
     * @param session
     * @return
     */
    @RequestMapping("/sessionTest")
    public String sessionTest(HttpSession session) {
        String test = (String) session.getAttribute("test");
        if (test != null) {
            return test + port;
        } else {
            session.setAttribute("test", "test");
        }
        return "ok";
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
