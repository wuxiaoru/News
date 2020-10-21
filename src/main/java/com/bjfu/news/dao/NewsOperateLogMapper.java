package com.bjfu.news.dao;

import com.bjfu.news.entity.NewsOperateLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewsOperateLogMapper {

    List<NewsOperateLog> selectByCId(Long id);

    NewsOperateLog loadById(Long id);

    int insertLog(NewsOperateLog log);

}
