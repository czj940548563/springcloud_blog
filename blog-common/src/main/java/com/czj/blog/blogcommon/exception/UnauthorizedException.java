package com.czj.blog.blogcommon.exception;

/**
 * @Author: clownc
 * @Date: 2019-04-26 14:31
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) {
        super(msg);
    }

    public UnauthorizedException() {
        super();
    }
}
