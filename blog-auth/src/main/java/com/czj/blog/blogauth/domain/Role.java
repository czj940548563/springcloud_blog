package com.czj.blog.blogauth.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: clownc
 * @Date: 2019-01-28 17:05
 */
public class Role implements Serializable {

    private static final long serialVersionUID = 7749966648800321871L;
    private Long id;
    private String roleName;
    private String description;
    private Date creatTime;
    private Date modifyTime;
    private List<Right> rights;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public List<Right> getRights() {
        return rights;
    }

    public void setRights(List<Right> rights) {
        this.rights = rights;
    }
}
