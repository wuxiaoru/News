package com.bjfu.news.controller;

import com.bjfu.news.constant.UserRoleType;
import com.bjfu.news.entity.NewsUserInfo;
import com.bjfu.news.entity.NewsUserRole;
import com.bjfu.news.model.UserList;
import com.bjfu.news.req.UserInfoCreateParam;
import com.bjfu.news.req.UserInfoEditParam;
import com.bjfu.news.req.UserReq;
import com.bjfu.news.untils.MapMessage;
import com.neusoft.education.tp.sso.client.filter.CASFilterRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@CrossOrigin
public class UserInfoController extends AbstractNewsController {

    //新增
    @RequestMapping(value = "/v1/user/info/create.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage create(@Validated @RequestBody UserInfoCreateParam param) {
        if (param.getRoleType() == null || param.getRoleType().isEmpty() || !UserRoleType.NAME_MAPPING.containsKey(param.getRoleType())) {
            return MapMessage.errorMessage().add("info", "角色有误");
        }
        MapMessage check = check(param);
        if (!check.isSuccess()) {
            return check;
        }
        MapMessage result = newsUserInfoService.createUserInfo(param);
        if (!result.isSuccess()) {
            return MapMessage.errorMessage().add("info", "添加失败");
        }
        return MapMessage.successMessage();
    }

    //添加审批人和编辑人
    @RequestMapping(value = "/v1/user/info/create-role.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage createRole(@Validated @RequestBody UserInfoCreateParam param) {
        if (param.getRoleType() == null || param.getRoleType().isEmpty() || !UserRoleType.NAME_MAPPING.containsKey(param.getRoleType())) {
            return MapMessage.errorMessage().add("info", "角色有误");
        }
        if (StringUtils.isEmpty(param.getEno())) {
            return MapMessage.errorMessage().add("info", "职工号有误");
        }
        NewsUserInfo userInfo = newsUserInfoLoader.loadByEno(param.getEno());
        if (Objects.isNull(userInfo)) {
            return MapMessage.errorMessage().add("info", "职工号在系统中不存在，请先在系统中登录");
        }
        List<NewsUserRole> newsUserRoles = newsUserInfoLoader.loadByUserId(userInfo.getId());
        List<NewsUserRole> collect = newsUserRoles.stream().filter(e -> e.getRole().equals(param.getRoleType())).collect(Collectors.toList());
        if (collect.size() > 0) {
            return MapMessage.errorMessage().add("info", "已经添加过此角色");
        }
        NewsUserRole userRole = new NewsUserRole();
        userRole.setRole(param.getRoleType());
        userRole.setUserId(userInfo.getId());
        userRole.setDisabled(false);
        NewsUserRole result = newsUserInfoService.insertRole(userRole);
        if (result == null) {
            return MapMessage.errorMessage().add("info", "添加失败");
        }
        return MapMessage.successMessage();
    }

    //列表
    @RequestMapping(value = "/v1/user/info/list.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage insert(@Validated @RequestBody UserReq req) {
        if (req.getRoleType() == null || req.getRoleType().isEmpty() || !UserRoleType.NAME_MAPPING.containsKey(req.getRoleType())) {
            return MapMessage.errorMessage().add("info", "角色有误");
        }
        int size = req.getSize() != null ? req.getSize() : 10;
        int page = req.getPage() != null ? req.getPage() : 0;
        req.setStart(Math.max((page - 1) * size, 0));
        req.setSize(size);
        int count = newsUserInfoLoader.getCount(req);
        List<NewsUserInfo> userInfos = newsUserInfoLoader.list(req);
        List<UserList> list = new ArrayList<>();
        for (NewsUserInfo userInfo : userInfos) {
            UserList userList = new UserList();
            BeanUtils.copyProperties(userInfo, userList);
            list.add(userList);
        }
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> finalMap = getFinalMap(count, size, page, list, map);
        return MapMessage.successMessage().add("data", finalMap);
    }

    @RequestMapping(value = "/v1/user/info/delete.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage delete(@Validated @NotNull @Min(value = 1, message = "id必须大于0") Long id, @Validated @NotNull String roleType) {
        NewsUserInfo newsUserInfo = newsUserInfoLoader.loadById(id);
        if (Objects.isNull(newsUserInfo)) {
            return MapMessage.errorMessage().add("info", "id有误");
        }
        List<NewsUserRole> newsUserRoles = newsUserInfoLoader.loadByUserId(id);
        List<NewsUserRole> collect = newsUserRoles.stream().filter(e -> e.getRole().contains(roleType)).collect(Collectors.toList());
        if (collect.size() < 1) {
            return MapMessage.errorMessage().add("info", "用户没有这个角色");
        }
        int delete = newsUserInfoService.delete(collect.get(0).getId());
        if (delete <= 0) {
            return MapMessage.errorMessage().add("info", "删除失败");
        }
        return MapMessage.successMessage();
    }

    @RequestMapping(value = "/v1/user/info/edit.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage edit(@Validated @RequestBody UserInfoCreateParam param) {
        if (param.getId() == null || param.getId() <= 0) {
            return MapMessage.errorMessage().add("info", "id有误");
        }
        NewsUserInfo newsUserInfo = newsUserInfoLoader.loadById(param.getId());
        if (Objects.isNull(newsUserInfo)) {
            return MapMessage.errorMessage().add("info", "id有误");
        }
        MapMessage check = check(param);
        if (!check.isSuccess()) {
            return check;
        }
        BeanUtils.copyProperties(param, newsUserInfo);
        int result = newsUserInfoService.update(newsUserInfo);
        if (result <= 0) {
            return MapMessage.errorMessage().add("info", "编辑失败");
        }
        return MapMessage.successMessage();
    }

    @RequestMapping(value = "/v1/user/info/eno/edit.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage edit(@Validated @RequestBody UserInfoEditParam param) {
        NewsUserInfo newsUserInfo = newsUserInfoLoader.loadByEno(param.getEno());
        if (Objects.isNull(newsUserInfo)) {
            return MapMessage.errorMessage().add("info", "职工号有误");
        }
        BeanUtils.copyProperties(param, newsUserInfo);
        int result = newsUserInfoService.update(newsUserInfo);
        if (result <= 0) {
            return MapMessage.errorMessage().add("info", "编辑失败");
        }
        return MapMessage.successMessage();
    }

    private MapMessage check(UserInfoCreateParam param) {
        if (param.getEno() == null || param.getEno().isEmpty()) {
            return MapMessage.errorMessage().add("info", "职工号有误");
        }

        if (param.getUserName() == null || param.getUserName().isEmpty()) {
            return MapMessage.errorMessage().add("info", "姓名有误");
        }

        if (param.getUnit() == null || param.getUnit().isEmpty()) {
            return MapMessage.errorMessage().add("info", "单位有误");
        }
        if (param.getMobile() == null || param.getMobile().isEmpty()) {
            return MapMessage.errorMessage().add("info", "手机号有误");
        }
        if (param.getOfficePhone() == null || param.getOfficePhone().isEmpty()) {
            return MapMessage.errorMessage().add("info", "办公电话有误");
        }
        if (param.getJob() == null || param.getJob().isEmpty()) {
            return MapMessage.errorMessage().add("info", "职务有误");
        }
        return MapMessage.successMessage();
    }

    @RequestMapping(value = "/v1/user/info/detail.vpage", method = RequestMethod.GET)
    @ResponseBody
    public MapMessage roleList(@Validated @NotNull String eno) {
        if (StringUtils.isEmpty(eno)) {
            return MapMessage.errorMessage().add("info", "职工号不能为空");
        }
        NewsUserInfo newsUserInfo = newsUserInfoLoader.loadByEno(eno);
        if (Objects.isNull(newsUserInfo)) {
            return MapMessage.errorMessage().add("info", "职工号在系统中不存在，请先在系统中登录");
        }
        return MapMessage.successMessage().add("userInfo", newsUserInfo);
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public MapMessage login(HttpServletRequest request) {
        String userId;
        try {
            CASFilterRequestWrapper reqWrapper = new CASFilterRequestWrapper(request);
            userId = reqWrapper.getRemoteUser();
        } catch (Exception e) {
            return MapMessage.errorMessage();
        }

        if (StringUtils.isEmpty(userId)) {
            return MapMessage.errorMessage();
        }
        NewsUserInfo newsUserInfo = newsUserInfoLoader.loadByEno(userId);
        List<String> roles = new ArrayList<>();
        if (Objects.isNull(newsUserInfo)) {
            return MapMessage.successMessage().add("role", roles).add("eno", userId);
        }
        List<NewsUserRole> newsUserRoles = newsUserInfoLoader.loadByUserId(newsUserInfo.getId());
        roles = newsUserRoles.stream().map(NewsUserRole::getRole).collect(Collectors.toList());
        return MapMessage.successMessage().add("role", roles).add("userInfo", newsUserInfo);
    }
}
