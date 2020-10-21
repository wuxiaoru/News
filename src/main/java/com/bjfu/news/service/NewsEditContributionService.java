package com.bjfu.news.service;

import com.bjfu.news.entity.NewsEditContribution;

public interface NewsEditContributionService {

    int update(NewsEditContribution newsEditContribution);

    int create(NewsEditContribution newsEditContribution);

}
