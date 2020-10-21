package com.bjfu.news.service.impl;

import com.bjfu.news.dao.NewsContributionMapper;
import com.bjfu.news.entity.NewsContribution;
import com.bjfu.news.req.ContributionReq;
import com.bjfu.news.service.NewsWriterContributionLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class NewsWriterContributionLoaderImpl implements NewsWriterContributionLoader {

    @Autowired
    NewsContributionMapper newsContributionMapper;

    @Override
    public List<NewsContribution> page(ContributionReq req) {
        try {
            return newsContributionMapper.page(req);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<NewsContribution> list(ContributionReq req) {
        return newsContributionMapper.list(req);
    }

    @Override
    public int getCount(ContributionReq req) {
        try {
            return newsContributionMapper.getCount(req);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public NewsContribution selectById(Long id) {
        try {
            return newsContributionMapper.selectById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<NewsContribution> selectByIds(Collection<Long> ids) {
        return newsContributionMapper.selectByIds(ids);
    }
}
