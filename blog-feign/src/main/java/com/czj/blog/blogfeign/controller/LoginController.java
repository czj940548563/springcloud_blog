package com.czj.blog.blogfeign.controller;

import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogcommon.exception.CustomException;
import com.czj.blog.blogcommon.exception.CustomUnauthorizedException;
import com.czj.blog.blogcommon.utils.JwtUtil;
import com.czj.blog.blogcommon.utils.MD5Util;
import com.czj.blog.blogcommon.utils.ResponseBean;
import com.czj.blog.blogfeign.result.Result;
import com.czj.blog.blogfeign.service.SchedualBlogAuth;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: clownc
 * @Date: 2019-04-24 15:53
 */
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private SchedualBlogAuth schedualBlogAuth;

    @PostMapping("/login")
    public ResponseBean login(@RequestBody User user, HttpServletResponse httpServletResponse) throws Exception {
        String account = user.getAccount();
        String password = user.getPassword();
        User userBean = schedualBlogAuth.selectUserByAccount(account);
        //md5
        if (MD5Util.verify(account,password,userBean.getPassword())){
            String token = JwtUtil.sign(account, password);
            httpServletResponse.setHeader("Authorization", token);
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
            return new ResponseBean(HttpStatus.OK.value(), "登录成功", userBean);
        }else {
            throw new CustomException("帐号或密码错误");
        }
    }


    /**
     * 测试登录
     * @param
     * @return com.wang.model.common.ResponseBean
     * @author Wang926454
     * @date 2018/8/30 16:18
     */
    @GetMapping("/article")
    public ResponseBean article() {
        Subject subject = SecurityUtils.getSubject();
        // 登录了返回true
        if (subject.isAuthenticated()) {
            return new ResponseBean(HttpStatus.OK.value(), "您已经登录了(You are already logged in)", null);
        } else {
            return new ResponseBean(HttpStatus.OK.value(), "你是游客(You are guest)", null);
        }
    }

}
