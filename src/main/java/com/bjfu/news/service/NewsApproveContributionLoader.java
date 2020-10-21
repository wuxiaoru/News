package com.bjfu.news.service;

import com.bjfu.news.entity.NewsApproveContribution;

import java.util.List;

public interface NewsApproveContributionLoader {

    NewsApproveContribution selectByCId(Long contributionId);

    List<NewsApproveContribution> selectByCIds(List<Long> contributionIds);

    List<NewsApproveContribution> selectByDate(String startTime, String endTime);

    NewsApproveContribution selectById(Long id);

    List<NewsApproveContribution> selectByApproveId(Long userId);
}
