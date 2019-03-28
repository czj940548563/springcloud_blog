package com.czj.blog.blogauth.dao;






import com.czj.blog.blogauth.domain.Right;

import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-01-29 15:25
 */

public interface RightDao {
    public Right selectRIghtById(Long id);
    public Integer insertRIght(Right right);
    public Integer updateRIght(Right right);
    public Integer deleteRIght(Long id);
    public Integer deleteRIghts(List<Long> ids);
}
