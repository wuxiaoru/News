package com.bjfu.news.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewsUserRole implements Serializable {

    private Long id;

    private Long userId;

    private String role;

    private Boolean disabled;

    private Date createTime;

    private Date updateTime;
}
