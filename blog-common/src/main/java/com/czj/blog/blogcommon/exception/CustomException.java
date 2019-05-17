package com.czj.blog.blogcommon.exception;

/**
 * @Author: clownc
 * @Date: 2019-04-26 14:26
 * 自定义异常
 */
public class CustomException extends RuntimeException   {
    public CustomException(String msg){
        super(msg);
    }

    public CustomException() {
        super();
    }
}
