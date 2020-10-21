package com.bjfu.news.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class OperateLogBean implements Serializable {

    private String docAuthor;
    private String docUrl;
    private String picAuthor;
    private String picUrl;
    private String suggestion;

}
