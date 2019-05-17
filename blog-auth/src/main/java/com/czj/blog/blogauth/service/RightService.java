package com.czj.blog.blogauth.service;

import com.czj.blog.blogauth.domain.Right;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-04-17 9:36
 */
public interface RightService {
    Right selectRight(Right right);

    PageInfo selectAllRight(int pageNum, int pageSize);

    Integer insertRight(Right right);

    Integer updateRight(Right right);

    Integer deleteRights(List<String> ids);

    List<String> selectRoleIdByRightId(List<String> ids);

    Right selectRightByName(String name);

}
