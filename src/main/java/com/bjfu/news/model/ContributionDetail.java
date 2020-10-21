package com.bjfu.news.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ContributionDetail implements Serializable {
    private Long id;
    private String title;
    private String status;
    private String docUrl;
    private String docAuthor;
    private String picUrl;
    private String picAuthor;
    private String note;
    private String submitTime;
    private Long approveId;
    private String approveName;
    private Long editorId;
    private String editorName;
    private String approveSuggestion;
    private String approveTime;
    private String editSuggestion;
    private String editTime;
    private List<OperateLogDetail> logList;
}
