package com.bjfu.news.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {
    private Long id;
    private String name;
}
