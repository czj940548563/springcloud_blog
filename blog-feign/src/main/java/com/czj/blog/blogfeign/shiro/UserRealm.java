package com.czj.blog.blogfeign.shiro;

import com.czj.blog.blogauth.domain.Right;
import com.czj.blog.blogauth.domain.Role;
import com.czj.blog.blogauth.domain.User;
import com.czj.blog.blogcommon.utils.JwtUtil;
import com.czj.blog.blogfeign.service.SchedualBlogAuth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-04-26 15:30
 */
@Service
public class UserRealm extends AuthorizingRealm {
    private static final Logger LOGGER = LogManager.getLogger(UserRealm.class);

    @Autowired
    private SchedualBlogAuth schedualBlogAuth;

    /**
     * 大坑，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String account = JwtUtil.getAccount(principals.toString());
        User user = schedualBlogAuth.selectUserByAccount(account);
        List<Role> roles = user.getRoles();
        if (roles.size() > 0) {
            for (Role role : roles) {
                //添加角色
                simpleAuthorizationInfo.addRole(role.getRoleName());
                List<Right> rights = role.getRights();
                if (rights.size() > 0) {
                    for (Right right : rights) {
                        //添加权限
                        simpleAuthorizationInfo.addStringPermission(right.getRightCode());
                    }
                }
            }
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String account = JwtUtil.getAccount(token);
        if (account == null) {
            throw new AuthenticationException("token invalid");
        }

        User userBean = schedualBlogAuth.selectUserByAccount(account);
        if (userBean == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        if (!JwtUtil.verify(token, account, userBean.getPassword())) {
            throw new AuthenticationException("请重新登录");
        }

        return new SimpleAuthenticationInfo(token, token, "userRealm");
    }
}
