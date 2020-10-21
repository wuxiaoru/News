package com.bjfu.news.service;

        import com.bjfu.news.entity.NewsOperateLog;

public interface NewsLogService {

    NewsOperateLog insertLog(NewsOperateLog log);

    NewsOperateLog createLog(String operateType, Long operateId, Long cId, String status, String docAuthor, String docUrl, String picAuthor, String picUrl, String suggestion);
}
