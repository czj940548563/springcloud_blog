package com.czj.blog.blogfeign.shiro;


import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Author: clownc
 * @Date: 2019-04-26 15:22
 */
public class JwtToken implements AuthenticationToken {

    // 密钥
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
