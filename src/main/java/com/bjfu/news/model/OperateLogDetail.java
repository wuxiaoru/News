package com.bjfu.news.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class OperateLogDetail implements Serializable {

    private Long id;
    private String operateTime;
    private Long operateId;
    private String operateName;
    private String docAuthor;
    private String docUrl;
    private String picAuthor;
    private String picUrl;
    private String suggestion;
    private String status;

}
