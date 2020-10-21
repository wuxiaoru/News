package com.bjfu.news.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserList implements Serializable {
    private Long id;

    private String eno;

    private String userName;

    private String unit;

    private String job;

    private String mail;

    private String officePhone;

    private String mobile;
}
