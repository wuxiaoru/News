package com.bjfu.news.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApproveList implements Serializable {

    private Long id;
    private Long contributionId;
    private String title;
    private String status;

}
