package com.bjfu.news.service;

import com.bjfu.news.entity.NewsContribution;
import com.bjfu.news.req.ContributionReq;

import java.util.Collection;
import java.util.List;

public interface NewsWriterContributionLoader {

    List<NewsContribution> page(ContributionReq req);

    List<NewsContribution> list(ContributionReq req);

    int getCount(ContributionReq req);

    NewsContribution selectById(Long id);

    List<NewsContribution> selectByIds(Collection<Long> ids);
}
