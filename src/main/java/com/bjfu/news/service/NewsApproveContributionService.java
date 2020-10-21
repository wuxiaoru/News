package com.bjfu.news.service;

import com.bjfu.news.entity.NewsApproveContribution;

public interface NewsApproveContributionService {

    int update(NewsApproveContribution approveContribution);

    NewsApproveContribution create(NewsApproveContribution contribution);
}
