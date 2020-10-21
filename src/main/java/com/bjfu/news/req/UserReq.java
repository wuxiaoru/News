package com.bjfu.news.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserReq implements Serializable {

    private String eno;

    private String userName;

    private String unit;

    private List<Long> userIds;

    private String roleType;

    private Integer start;

    private Integer page;

    private Integer size;
}
