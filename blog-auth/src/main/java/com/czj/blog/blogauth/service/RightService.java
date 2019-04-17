package com.czj.blog.blogauth.service;

import com.czj.blog.blogauth.domain.Right;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-04-17 9:36
 */
public interface RightService {
    public Right selectRight(Right right);
    public PageInfo selectAllRight(int pageNum, int pageSize);
    public Integer insertRight(Right right);
    public Integer updateRight(Right right);
    public Integer deleteRights(List<String> ids);
}
