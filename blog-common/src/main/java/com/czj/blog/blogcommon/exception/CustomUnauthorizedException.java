package com.czj.blog.blogcommon.exception;

/**
 * 自定义401无权限异常(UnauthorizedException)
 * @Author: clownc
 * @Date: 2019-04-26 14:31
 */
public class CustomUnauthorizedException extends RuntimeException {

    public CustomUnauthorizedException(String msg){
        super(msg);
    }

    public CustomUnauthorizedException() {
        super();
    }
}
