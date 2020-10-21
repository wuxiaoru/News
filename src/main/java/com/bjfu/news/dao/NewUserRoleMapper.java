package com.bjfu.news.dao;

import com.bjfu.news.entity.NewsUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewUserRoleMapper {

    int insertUserRole(NewsUserRole record);

    List<NewsUserRole> loadByRole(String roleType);

    List<NewsUserRole> loadByUserId(Long userId);

    int update(NewsUserRole newsUserRole);

}
