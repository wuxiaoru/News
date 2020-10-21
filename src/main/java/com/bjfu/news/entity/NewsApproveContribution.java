package com.bjfu.news.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewsApproveContribution implements Serializable {
    private Long id;

    private Long contributionId;

    private Long userId;

    private String suggestion;

    private Date approveTime;

    private Boolean disabled;

    private Date createTime;

    private Date updateTime;
}