package com.czj.blog.blogcommon.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.czj.blog.blogcommon.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @Author: clownc
 * @Date: 2019-04-26 10:53
 */
public class JwtUtil {
    /**
     * LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
    // 过期时间5分钟
    private static final long EXPIRE_TIME = 10*60*1000;

    /**
     * 校验token是否正确
     * @param token 密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String account, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);//不可逆加密
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("account", account)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getAccount(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("account").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     * @param account 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String sign(String account, String secret) {
        try {
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
            String md5 = MD5Util.md5(account, secret);//注册时用户密码加密了一次，生成token时也加密一次得到对应的字符串
            Algorithm algorithm = Algorithm.HMAC256(md5);
            // 附带username信息
            return JWT.create()
                    .withClaim("account", account)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("JWTToken加密出现UnsupportedEncodingException异常:" + e.getMessage());
            throw new CustomException("JWTToken加密出现UnsupportedEncodingException异常:" + e.getMessage());
        }
    }
}
