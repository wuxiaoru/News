package com.bjfu.news.service;

import com.bjfu.news.entity.NewsContribution;
import com.bjfu.news.req.ContributionCreateParam;
import com.bjfu.news.untils.MapMessage;

public interface NewsWriterContributionService {

    int delete(Long id);

    int update(NewsContribution newsContribution);

    NewsContribution create(NewsContribution contribution);

    MapMessage submitContribution(ContributionCreateParam param);

    MapMessage fastSubmit(Long id, Long approveId, Long userId);

    MapMessage saveDraft(ContributionCreateParam param);
}
