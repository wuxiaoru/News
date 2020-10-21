package com.bjfu.news.service;

import com.bjfu.news.entity.NewsUserInfo;
import com.bjfu.news.entity.NewsUserRole;
import com.bjfu.news.req.UserReq;

import java.util.List;

public interface NewsUserInfoLoader {

    List<NewsUserInfo> list(UserReq req);

    List<NewsUserInfo> page(UserReq req);

    int getCount(UserReq req);

    NewsUserInfo loadById(Long userId);

    NewsUserInfo loadInCluDisableById(Long userId);

    List<NewsUserInfo> loadByIds(List<Long> userId);

    List<NewsUserRole> loadByUserId(Long userId);

    NewsUserInfo loadByEno(String eno);
}
