package com.czj.blog.blogfeign.result;

/**
 * @Author: clownc
 * @Date: 2019-03-28 9:26
 */
public class CodeMsg {
    private int code;
    private String msg;

    //通用的错误码
    public static CodeMsg SUCCESS = new CodeMsg(200, "success");
    public static CodeMsg UNAUTHORIZED = new CodeMsg(401, "无权访问");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg ACCESS_LIMIT_REACHED= new CodeMsg(500104, "访问高峰期，请稍等！");
    public static CodeMsg USER_EXITS= new CodeMsg(500105, "用户已存在！");
    public static CodeMsg ROLE_EXITS= new CodeMsg(500106, "角色已存在！");
    public static CodeMsg RIGHT_EXITS= new CodeMsg(500106, "权限已存在！");
    public static CodeMsg ROLE_BOUND= new CodeMsg(500107, "存在角色被绑定在用户中，不可删除！");
    public static CodeMsg RIGHT_BOUND= new CodeMsg(500108, "存在权限被绑定在角色中，不可删除！");
    //登录模块 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
    public static CodeMsg ACCOUNT_NOTEXITS = new CodeMsg(500211, "用户不存在");

    private CodeMsg() {
    }

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 返回带参数的错误码
     * @param args
     * @return
     */
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", msg=" + msg + "]";
    }
}
