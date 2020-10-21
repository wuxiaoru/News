package com.bjfu.news.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewsContribution implements Serializable {
    private Long id;

    private String title;

    private String status;

    private String docUrl;

    private String docAuthor;

    private String picUrl;

    private String picAuthor;

    private String note;

    private Date submitTime;

    private Long userId;

    private Boolean disabled;

    private Date createTime;

    private Date updateTime;
}