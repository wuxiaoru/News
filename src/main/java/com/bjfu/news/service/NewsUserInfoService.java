package com.bjfu.news.service;

import com.bjfu.news.entity.NewsUserInfo;
import com.bjfu.news.req.UserInfoCreateParam;
import com.bjfu.news.untils.MapMessage;

public interface NewsUserInfoService {

    NewsUserInfo insert(NewsUserInfo newsUserInfo);

    MapMessage createUserInfo(UserInfoCreateParam param);

    int delete(Long id);

     int update(NewsUserInfo newsUserInfo);

}
