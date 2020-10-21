package com.bjfu.news.service.impl;

import com.bjfu.news.dao.NewsEditContributionMapper;
import com.bjfu.news.entity.NewsEditContribution;
import com.bjfu.news.service.NewsEditContributionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class NewsEditContributionServiceImpl implements NewsEditContributionService {

    @Autowired
    private NewsEditContributionMapper newsEditContributionMapper;

    @Override
    public int update(NewsEditContribution newsEditContribution) {
        try {
            return newsEditContributionMapper.update(newsEditContribution);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public int create(NewsEditContribution newsEditContribution) {
        try {
            NewsEditContribution editContribution = newsEditContributionMapper.selectByCId(newsEditContribution.getContributionId());
            if (Objects.nonNull(editContribution)) {
                newsEditContribution.setId(editContribution.getId());
                return update(newsEditContribution);
            }
            return newsEditContributionMapper.insert(newsEditContribution);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }
}
