package com.czj.blog.blogauth.dao;

import com.czj.blog.blogauth.domain.Right;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-01-29 15:25
 */

public interface RightDao {
    Right selectRight(Right right);
    List<Right> selectAllRight();
    Integer insertRight(Right right);
    Integer updateRight(Right right);
    Integer deleteRights(List<String> ids);

    List<String> selectRoleIdByRightId(List<String> ids);

    Right selectRightByName(String name);

}
