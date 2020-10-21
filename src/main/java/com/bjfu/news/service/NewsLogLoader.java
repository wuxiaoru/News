package com.bjfu.news.service;

import com.bjfu.news.entity.NewsOperateLog;

import java.util.List;

public interface NewsLogLoader {

    List<NewsOperateLog> loadByCId(Long id);

    NewsOperateLog loadById(Long id);
}
