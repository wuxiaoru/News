package com.bjfu.news.dao;

import com.bjfu.news.entity.NewsContribution;
import com.bjfu.news.req.ContributionReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface NewsContributionMapper {
    int insert(NewsContribution record);

    int update(NewsContribution record);

    NewsContribution selectById(Long id);

    List<NewsContribution> selectByIds(Collection<Long> ids);

    List<NewsContribution> list(ContributionReq req);

    List<NewsContribution> page(ContributionReq req);

    int getCount(ContributionReq req);

    int delete(Long id);
}