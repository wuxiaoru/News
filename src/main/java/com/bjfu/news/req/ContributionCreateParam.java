package com.bjfu.news.req;


import lombok.Data;

import java.io.Serializable;

@Data
public class ContributionCreateParam implements Serializable {

    private String title;
    private String docUrl;
    private String docAuthor;
    private Long approveId;
    private String picUrl;
    private String picAuthor;
    private String note;
    private Long userId;
}
