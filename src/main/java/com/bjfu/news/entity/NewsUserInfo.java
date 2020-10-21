package com.bjfu.news.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewsUserInfo implements Serializable {
    private Long id;

    private String eno;

    private String userName;

    private String unit;

    private String job;

    private String mail;

    private String officePhone;

    private String mobile;

    private Boolean disabled;

    private Date createTime;

    private Date updateTime;

}
