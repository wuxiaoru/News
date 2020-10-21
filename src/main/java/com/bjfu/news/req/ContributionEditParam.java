package com.bjfu.news.req;


import lombok.Data;

import java.io.Serializable;

@Data
public class ContributionEditParam implements Serializable {

    private Long id;
    private String title;
    private String docUrl;
    private String docAuthor;
    private String picUrl;
    private String picAuth;
    private String note;
    private Long userId;
}
