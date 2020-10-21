package com.bjfu.news.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoCreateParam implements Serializable {

    private Long id;

    private String eno;

    private String userName;

    private String unit;

    private String job;

    private String mail;

    private String officePhone;

    private String mobile;

    private String roleType;
}
