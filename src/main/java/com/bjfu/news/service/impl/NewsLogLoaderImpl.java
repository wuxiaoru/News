package com.bjfu.news.service.impl;

import com.bjfu.news.dao.NewsOperateLogMapper;
import com.bjfu.news.entity.NewsOperateLog;
import com.bjfu.news.service.NewsLogLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NewsLogLoaderImpl implements NewsLogLoader {

    @Autowired
    private NewsOperateLogMapper newsOperateLogMapper;

    @Override
    public List<NewsOperateLog> loadByCId(Long id) {
        return newsOperateLogMapper.selectByCId(id);
    }

    @Override
    public NewsOperateLog loadById(Long id) {
        return newsOperateLogMapper.loadById(id);
    }
}
