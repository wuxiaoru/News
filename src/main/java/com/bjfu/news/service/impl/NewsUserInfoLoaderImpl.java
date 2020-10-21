package com.bjfu.news.service.impl;

import com.bjfu.news.dao.NewUserRoleMapper;
import com.bjfu.news.dao.NewsUserInfoMapper;
import com.bjfu.news.entity.NewsUserInfo;
import com.bjfu.news.entity.NewsUserRole;
import com.bjfu.news.req.UserReq;
import com.bjfu.news.service.NewsUserInfoLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NewsUserInfoLoaderImpl implements NewsUserInfoLoader {

    @Autowired
    private NewUserRoleMapper newUserRoleMapper;

    @Autowired
    private NewsUserInfoMapper newsUserInfoMapper;

    @Override
    public List<NewsUserInfo> list(UserReq req) {
        UserReq userReq = getReq(req);
        if (Objects.isNull(userReq)) {
            return Collections.emptyList();
        }
        return newsUserInfoMapper.list(req);
    }

    @Override
    public List<NewsUserInfo> page(UserReq req) {
        UserReq userReq = getReq(req);
        if (Objects.isNull(userReq)) {
            return Collections.emptyList();
        }
        return newsUserInfoMapper.page(req);
    }

    @Override
    public int getCount(UserReq req) {
        UserReq userReq = getReq(req);
        if (Objects.isNull(userReq)) {
            return 0;
        }
        return newsUserInfoMapper.getCount(req);
    }

    private UserReq getReq(UserReq req) {
        List<NewsUserRole> userIds = newUserRoleMapper.loadByRole(req.getRoleType());
        if (CollectionUtils.isEmpty(userIds)) {
            return null;
        }
        req.setUserIds(userIds.stream().map(NewsUserRole::getUserId).collect(Collectors.toList()));
        return req;
    }

    @Override
    public NewsUserInfo loadById(Long userId) {
        if (userId == null) {
            return null;
        }
        return newsUserInfoMapper.selectById(userId);
    }

    @Override
    public NewsUserInfo loadInCluDisableById(Long userId) {
        if (userId == null) {
            return null;
        }
        return newsUserInfoMapper.selectInCluDisableById(userId);
    }

    @Override
    public List<NewsUserInfo> loadByIds(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return newsUserInfoMapper.selectByIds(userIds);
    }

    @Override
    public List<NewsUserRole> loadByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return newUserRoleMapper.loadByUserId(userId);
    }

    @Override
    public NewsUserInfo loadByEno(String eno) {
        if (StringUtils.isEmpty(eno)) {
            return null;
        }
        return newsUserInfoMapper.selectByEno(eno);
    }
}
