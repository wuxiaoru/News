package com.bjfu.news.service.impl;

import com.bjfu.news.dao.NewsApproveContributionMapper;
import com.bjfu.news.entity.NewsApproveContribution;
import com.bjfu.news.service.NewsApproveContributionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class NewsApproveContributionServiceImpl implements NewsApproveContributionService {

    @Autowired
    private NewsApproveContributionMapper newsApproveContributionMapper;

    @Override
    public int update(NewsApproveContribution approveContribution) {
        try {
            return newsApproveContributionMapper.update(approveContribution);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public NewsApproveContribution create(NewsApproveContribution contribution) {
        if (contribution.getId() == null) {
            try {
                newsApproveContributionMapper.insert(contribution);
                return contribution;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        NewsApproveContribution approveContribution = newsApproveContributionMapper.selectByCId(contribution.getContributionId());
        if (Objects.nonNull(approveContribution)) {
            BeanUtils.copyProperties(contribution, approveContribution);
            newsApproveContributionMapper.update(contribution);
            return contribution;
        }
        return null;
    }
}
