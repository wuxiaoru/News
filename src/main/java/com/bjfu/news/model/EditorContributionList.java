package com.bjfu.news.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class EditorContributionList implements Serializable {

    private Long id;

    private String title;

    private String status;

    private String docAuthor;

    private String unit;

    private String approveName;

    private String approveTime;
}
