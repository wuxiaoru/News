package com.bjfu.news.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewsOperateLog implements Serializable {

    private Long id;

    private Long contributionId;

    private String status;

    private String operateDetail;

    private String operateType;

    private Long operateId;

    private Date operateTime;

}
