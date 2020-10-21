package com.bjfu.news.service.impl;

import com.alibaba.fastjson.JSON;
import com.bjfu.news.dao.NewsOperateLogMapper;
import com.bjfu.news.entity.NewsOperateLog;
import com.bjfu.news.model.OperateLogBean;
import com.bjfu.news.service.NewsLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NewsLogServiceImpl implements NewsLogService {

    @Autowired
    private NewsOperateLogMapper newsOperateLogMapper;

    @Override
    public NewsOperateLog insertLog(NewsOperateLog log) {
        newsOperateLogMapper.insertLog(log);
        return log;
    }

    @Override
    public NewsOperateLog createLog(String operateType, Long operateId, Long cId, String status, String docAuthor, String docUrl, String picAuthor, String picUrl, String suggestion) {
        NewsOperateLog log = new NewsOperateLog();
        log.setOperateType(operateType);
        log.setOperateId(operateId);
        log.setContributionId(cId);
        log.setStatus(status);
        OperateLogBean bean = new OperateLogBean();
        bean.setDocAuthor(docAuthor);
        bean.setDocUrl(docUrl);
        if (picAuthor != null) {
            bean.setPicAuthor(picAuthor);
        }
        if (picUrl != null) {
            bean.setPicUrl(picUrl);
        }
        if (suggestion != null) {
            bean.setSuggestion(suggestion);
        }
        log.setOperateDetail(JSON.toJSONString(bean));
        insertLog(log);
        return log;
    }
}
