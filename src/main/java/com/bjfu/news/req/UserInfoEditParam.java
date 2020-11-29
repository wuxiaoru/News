package com.bjfu.news.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoEditParam implements Serializable {

    private String eno;

    private String unit;

    private String job;

    private String mail;

    private String officePhone;

    private String mobile;
}
