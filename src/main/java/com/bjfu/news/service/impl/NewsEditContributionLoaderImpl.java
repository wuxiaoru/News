package com.bjfu.news.service.impl;

import com.bjfu.news.dao.NewsEditContributionMapper;
import com.bjfu.news.entity.NewsEditContribution;
import com.bjfu.news.req.ContributionReq;
import com.bjfu.news.service.NewsEditContributionLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsEditContributionLoaderImpl implements NewsEditContributionLoader {

    @Autowired
    private NewsEditContributionMapper newsEditContributionMapper;

    @Override
    public NewsEditContribution selectByCId(Long contributionId) {
        return newsEditContributionMapper.selectByCId(contributionId);
    }

    @Override
    public NewsEditContribution selectById(Long id) {
        return newsEditContributionMapper.selectById(id);
    }

    @Override
    public List<NewsEditContribution> list(ContributionReq req) {
        return newsEditContributionMapper.list(req);
    }
}
