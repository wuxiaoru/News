package com.bjfu.news.entity;

import lombok.Data;

import java.util.Date;

@Data
public class NewsEditContribution {
    private Long id;

    private Long userId;

    private Long contributionId;

    private String suggestion;

    private Boolean disabled;

    private Date createTime;

    private Date updateTime;

}