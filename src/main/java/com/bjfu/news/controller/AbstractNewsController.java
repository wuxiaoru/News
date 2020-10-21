package com.bjfu.news.controller;

import com.bjfu.news.entity.NewsContribution;
import com.bjfu.news.model.ContributionList;
import com.bjfu.news.req.ContributionReq;
import com.bjfu.news.service.*;
import com.bjfu.news.untils.DateUtils;
import com.bjfu.news.untils.MapMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Slf4j
public abstract class AbstractNewsController {

    @Autowired
    protected NewsApproveContributionLoader approveContributionLoader;

    @Autowired
    protected NewsApproveContributionService approveContributionService;

    @Autowired
    protected NewsEditContributionService editContributionService;

    @Autowired
    protected NewsEditContributionLoader newsEditContributionLoader;

    @Autowired
    protected NewsWriterContributionService newsWriterContributionService;

    @Autowired
    protected NewsWriterContributionLoader newsWriterContributionLoader;

    @Autowired
    protected NewsUserInfoService newsUserInfoService;

    @Autowired
    protected NewsUserInfoLoader newsUserInfoLoader;

    @Autowired
    protected NewsLogLoader newsLogLoader;

    @Autowired
    protected NewsLogService newsLogService;

    protected MapMessage list(ContributionReq req, Map<String, Object> map) {
        int size = req.getSize() != null ? req.getSize() : 10;
        int page = req.getPage() != null ? req.getPage() : 0;
        req.setStart((page - 1) * size > 0 ? (page - 1) * size : 0);
        req.setSize(size);
        int count = newsWriterContributionLoader.getCount(req);
        List<NewsContribution> writerContributions = newsWriterContributionLoader.page(req);
        List<ContributionList> list = new ArrayList<>();
        for (NewsContribution contribution : writerContributions) {
            ContributionList contributionList = new ContributionList();
            BeanUtils.copyProperties(contribution, contributionList);
            contributionList.setSubmitTime(DateUtils.DateToString(contribution.getSubmitTime()));
            list.add(contributionList);
        }
        Map<String, Object> finalMap = getFinalMap(count, size, page, list, map);
        return MapMessage.successMessage().add("data", finalMap);
    }

    protected Map<String, Object> getInitialMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("list", Collections.emptyList());
        map.put("pageSize", 0);
        map.put("totalCount", 0);
        map.put("maxPage", 0);
        return map;
    }

    protected Map<String, Object> getFinalMap(int count, int size, int page, List list, Map<String, Object> map) {
        int maxPage = count % size == 0 ? count / size : count / size + 1;
        map.put("list", list);
        map.put("pageSize", page);
        map.put("totalCount", count);
        map.put("maxPage", maxPage);
        return map;
    }

}
