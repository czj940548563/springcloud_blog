package com.czj.blog.blogauth.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.context.annotation.Lazy;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: clownc
 * @Date: 2019-01-28 17:05
 */

@JsonIgnoreProperties(value = {"handler"})
public class Right implements Serializable {

    private static final long serialVersionUID = -4601859381069240541L;
    private Long id;
    private String rightName;
    private String description;
    private Date creatTime;
    private Date modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
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
}
