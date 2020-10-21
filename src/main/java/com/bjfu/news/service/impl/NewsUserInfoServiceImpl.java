package com.bjfu.news.service.impl;

import com.bjfu.news.dao.NewUserRoleMapper;
import com.bjfu.news.dao.NewsUserInfoMapper;
import com.bjfu.news.entity.NewsUserInfo;
import com.bjfu.news.entity.NewsUserRole;
import com.bjfu.news.req.UserInfoCreateParam;
import com.bjfu.news.service.NewsUserInfoService;
import com.bjfu.news.untils.MapMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class NewsUserInfoServiceImpl implements NewsUserInfoService {

    @Autowired
    private NewsUserInfoMapper newsUserInfoMapper;

    @Autowired
    private NewUserRoleMapper newUserRoleMapper;

    @Override
    public NewsUserInfo insert(NewsUserInfo newsUserInfo) {
        try {
            NewsUserInfo info = newsUserInfoMapper.selectByEno(newsUserInfo.getEno());
            if (Objects.nonNull(info)) {
                return info;
            }
            newsUserInfoMapper.insertUserInfo(newsUserInfo);
            return newsUserInfo;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    @Transactional
    public MapMessage createUserInfo(UserInfoCreateParam param) {
        NewsUserInfo newsUserInfo = new NewsUserInfo();
        BeanUtils.copyProperties(param, newsUserInfo);
        newsUserInfo.setDisabled(false);
        newsUserInfo.setCreateTime(new Date());
        newsUserInfo.setUpdateTime(new Date());
        NewsUserInfo userInfo = insert(newsUserInfo);
        NewsUserRole userRole = new NewsUserRole();
        userRole.setRole(param.getRoleType());
        userRole.setUserId(userInfo.getId());
        userRole.setDisabled(false);
        newUserRoleMapper.insertUserRole(userRole);
        return MapMessage.successMessage();
    }

    @Override
    @Transactional
    public int delete(Long id) {
        try {
            NewsUserInfo newsUserInfo = newsUserInfoMapper.selectById(id);
            if (Objects.nonNull(newsUserInfo)) {
                newsUserInfo.setDisabled(true);
                newsUserInfoMapper.update(newsUserInfo);
            }
            List<NewsUserRole> newsUserRoles = newUserRoleMapper.loadByUserId(id);
            if (!CollectionUtils.isEmpty(newsUserRoles)) {
                newsUserRoles.forEach(e -> {
                    e.setDisabled(true);
                    newUserRoleMapper.update(e);
                });
            }
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public int update(NewsUserInfo newsUserInfo) {
        try {
            newsUserInfoMapper.update(newsUserInfo);
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }
}
