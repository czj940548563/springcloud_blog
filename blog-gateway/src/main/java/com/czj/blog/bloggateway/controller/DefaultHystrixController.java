package com.czj.blog.bloggateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: clownc
 * @Date: 2019-03-28 11:22
 * 默认降级处理
 */
@RestController
public class DefaultHystrixController {
    @RequestMapping("/defaultfallback")
    public Map<String,String> defaultfallback(){
        Map<String,String> map = new HashMap<>();
        map.put("resultCode","fail");
        map.put("resultMessage","服务异常");
        map.put("resultObj","null");
        return map;
    }

}
