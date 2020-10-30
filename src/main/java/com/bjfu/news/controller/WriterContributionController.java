package com.bjfu.news.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjfu.news.constant.ContributionStatus;
import com.bjfu.news.constant.OperateType;
import com.bjfu.news.constant.UserRoleType;
import com.bjfu.news.entity.*;
import com.bjfu.news.model.ContributionDetail;
import com.bjfu.news.model.OperateLogBean;
import com.bjfu.news.model.OperateLogDetail;
import com.bjfu.news.model.UserInfo;
import com.bjfu.news.req.ContributionCreateParam;
import com.bjfu.news.req.ContributionEditParam;
import com.bjfu.news.req.ContributionReq;
import com.bjfu.news.req.UserReq;
import com.bjfu.news.untils.DateUtils;
import com.bjfu.news.untils.FileUtils;
import com.bjfu.news.untils.MapMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/v1/contribution")
@CrossOrigin
public class WriterContributionController extends AbstractNewsController {

    private static String LIN_ACCESS_PATH = "http://202.204.121.200:8561/file/";

    private static String WIN_ACCESS_PATH = "http://localhost:8561/file/";

    private static String WIN_FILE_PATH = "D:\\file\\";

    private static String LIN_FILE_PATH = "/usr/local/news/doc/";

    @RequestMapping(value = "list.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage list(@Validated @RequestBody ContributionReq req, HttpServletRequest request) {
        String eno = request.getHeader("userId");
        NewsUserInfo newsUserInfo = newsUserInfoLoader.loadByEno(eno);
        if (Objects.isNull(newsUserInfo)) {
            return MapMessage.errorMessage().add("info", "用户信息有误");
        }
        req.setUserId(newsUserInfo.getId());
        Map<String, Object> map = new HashMap<>();
        return list(req, map);
    }

    //上传
    @RequestMapping(value = "/upload.vpage",
            method = RequestMethod.POST)
    @ResponseBody
    public MapMessage upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        if (multipartRequest == null) {
            MapMessage.errorMessage().add("info", "请上传文件");
        }
        List<String> list = FileUtils.uploadFile(request);
        return MapMessage.successMessage().add("data", list);
    }

    //下载
    @RequestMapping(value = "/download.vpage",
            method = RequestMethod.GET)
    public void download(HttpServletResponse response, @Validated @NotNull @Min(value = 1, message = "id必须大于0") Long id) {
        NewsContribution newsContribution = newsWriterContributionLoader.selectById(id);
        if (Objects.isNull(newsContribution)) {
            return;
        }
        if (newsContribution.getDocUrl() == null || StringUtils.isEmpty(newsContribution.getDocUrl())) {
            return;
        }
        try {
            FileUtils.downloadLocal(response, newsContribution.getDocUrl());
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/log/download.vpage",
            method = RequestMethod.GET)
    public void logDownload(HttpServletResponse response, @Validated @NotNull @Min(value = 1, message = "id必须大于0") Long logId) {
        NewsOperateLog newsOperateLogs = newsLogLoader.loadById(logId);
        if (Objects.isNull(newsOperateLogs)) {
            return;
        }
        OperateLogBean jsonObject = JSONObject.toJavaObject(JSON.parseObject(newsOperateLogs.getOperateDetail()), OperateLogBean.class);
        if (Objects.isNull(jsonObject)) {
            return;
        }
        if (jsonObject.getDocUrl() == null || StringUtils.isEmpty(jsonObject.getDocUrl())) {
            return;
        }
        try {
            FileUtils.downloadLocal(response, jsonObject.getDocUrl());
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/pic/download.vpage",
            method = RequestMethod.GET)
    public void picDownload(HttpServletResponse response, @Validated @NotNull @Min(value = 1, message = "id必须大于0") Long id) {
        NewsContribution newsContribution = newsWriterContributionLoader.selectById(id);
        if (Objects.isNull(newsContribution)) {
            return;
        }
        if (newsContribution.getPicUrl() == null || StringUtils.isEmpty(newsContribution.getPicUrl())) {
            return;
        }
        try {
            List<String> list = Arrays.asList(newsContribution.getPicUrl().split(","));
            for (String picUrl : list) {
                FileUtils.downloadLocal(response, picUrl);
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/preview.vpage",
            method = RequestMethod.GET)
    @ResponseBody
    public MapMessage preview(@Validated @NotNull @Min(value = 1, message = "id必须大于0") Long id) throws FileNotFoundException {
        NewsContribution newsContribution = newsWriterContributionLoader.selectById(id);
        if (Objects.isNull(newsContribution)) {
            return MapMessage.errorMessage().add("info", "稿件id有误");
        }
        if (newsContribution.getDocUrl() == null || StringUtils.isEmpty(newsContribution.getDocUrl())) {
            return MapMessage.errorMessage().add("info", "此稿件还没上传文档");
        }
        String path = LIN_FILE_PATH;
        String accessPath = LIN_ACCESS_PATH;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            path = WIN_FILE_PATH;
            accessPath = WIN_ACCESS_PATH;
        }
        String[] split = StringUtils.split(newsContribution.getDocUrl(), ".");
        if (split == null || split.length < 1) {
            return MapMessage.errorMessage().add("info", "此稿件文档有误");
        }
        File file = new File(path + split[0] + ".pdf");
        if (!file.exists()) {
            OpenOffice2PdfUtils opc = new OpenOffice2PdfUtils();
            opc.convert2PDF(path + newsContribution.getDocUrl(), path + split[0] + ".pdf");
        }
        List<String> picUrls = new ArrayList<>();
        if (newsContribution.getPicUrl() != null && !StringUtils.isEmpty(newsContribution.getPicUrl())) {
            List<String> list = Arrays.asList(newsContribution.getPicUrl().split(","));
            if (!CollectionUtils.isEmpty(list)) {
                for (String url : list) {
                    picUrls.add(accessPath + url);
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("pdf", accessPath + split[0] + ".pdf");
        map.put("pic", picUrls);
        return MapMessage.successMessage().add("data", map);
    }

    @ResponseBody
    @RequestMapping(value = "submit.vpage", method = RequestMethod.POST)
    public MapMessage submit(@Validated @RequestBody ContributionCreateParam param) {
        MapMessage check = check(param);
        if (!check.isSuccess()) {
            return check;
        }
        if (param.getApproveId() == null || param.getApproveId() <= 0L) {
            return MapMessage.errorMessage().add("info", "审批人不能为空");
        }
        return newsWriterContributionService.submitContribution(param);
    }

    @ResponseBody
    @RequestMapping(value = "fast-submit.vpage", method = RequestMethod.POST)
    public MapMessage fastSubmit(@Validated @NotNull @Min(value = 1, message = "id必须大于0") Long id,
                                 @Validated @NotNull @Min(value = 1, message = "id必须大于0") Long approveId) {
        NewsContribution newsContribution = newsWriterContributionLoader.selectById(id);
        if (Objects.isNull(newsContribution)) {
            return MapMessage.errorMessage().add("info", "稿件id有误");
        }
        if (!newsContribution.getStatus().equals(ContributionStatus.DRAFT.name())) {
            return MapMessage.errorMessage().add("info", "当前稿件不是草稿状态，不能提交");
        }
        NewsUserInfo newsUserInfo = newsUserInfoLoader.loadById(approveId);
        if (Objects.isNull(newsUserInfo)) {
            return MapMessage.errorMessage().add("info", "审稿人id有误");
        }
        return newsWriterContributionService.fastSubmit(id, approveId);
    }

    @ResponseBody
    @RequestMapping(value = "draft.vpage", method = RequestMethod.POST)
    public MapMessage draft(@Validated @RequestBody ContributionCreateParam param) {
        MapMessage check = check(param);
        if (!check.isSuccess()) {
            return check;
        }
        return newsWriterContributionService.saveDraft(param);
    }

    private MapMessage check(ContributionCreateParam param) {
        if (param.getTitle() == null || param.getTitle().isEmpty()) {
            return MapMessage.errorMessage().add("info", "题目不能为空");
        }
        if (param.getDocUrl() == null || param.getDocUrl().isEmpty()) {
            return MapMessage.errorMessage().add("info", "稿件地址不能为空");
        }
        if (param.getDocAuthor() == null || param.getDocAuthor().isEmpty()) {
            return MapMessage.errorMessage().add("info", "稿件作者不能为空");
        }
        return MapMessage.successMessage();
    }


    //查看详情
    @RequestMapping(value = "detail.vpage", method = RequestMethod.GET)
    @ResponseBody
    public MapMessage detail(@Validated @NotNull @Min(value = 1, message = "id必须大于0") Long id) {
        NewsContribution newsContribution = newsWriterContributionLoader.selectById(id);
        if (Objects.isNull(newsContribution)) {
            return MapMessage.errorMessage().add("info", "id有误");
        }
        ContributionDetail detail = new ContributionDetail();
        BeanUtils.copyProperties(newsContribution, detail);
        detail.setSubmitTime(DateUtils.DateToString(newsContribution.getSubmitTime()));

        NewsApproveContribution newsApproveContribution = approveContributionLoader.selectByCId(newsContribution.getId());
        if (Objects.nonNull(newsApproveContribution)) {
            if (newsApproveContribution.getUserId() != null) {
                detail.setApproveId(newsApproveContribution.getUserId());
                NewsUserInfo newsUserInfo = newsUserInfoLoader.loadInCluDisableById(newsApproveContribution.getUserId());
                if (Objects.nonNull(newsUserInfo)) {
                    detail.setApproveName(newsUserInfo.getUserName());
                }
            }
            if (newsApproveContribution.getApproveTime() != null) {
                detail.setApproveTime(DateUtils.DateToString(newsApproveContribution.getApproveTime()));
            }
            if (newsApproveContribution.getSuggestion() != null && !newsApproveContribution.getSuggestion().isEmpty()) {
                detail.setApproveSuggestion(newsApproveContribution.getSuggestion());
            }
        }
        NewsEditContribution newsEditContribution = newsEditContributionLoader.selectByCId(newsContribution.getId());
        if (Objects.nonNull(newsEditContribution)) {
            if (newsEditContribution.getUserId() != null) {
                detail.setEditorId(newsEditContribution.getUserId());
                NewsUserInfo newsUserInfo = newsUserInfoLoader.loadInCluDisableById(newsEditContribution.getUserId());
                if (Objects.nonNull(newsUserInfo)) {
                    detail.setEditorName(newsUserInfo.getUserName());
                }
            }
            if (newsEditContribution.getSuggestion() != null && !newsEditContribution.getSuggestion().isEmpty()) {
                detail.setEditSuggestion(newsEditContribution.getSuggestion());
                if (newsEditContribution.getUpdateTime() != null) {
                    detail.setApproveTime(DateUtils.DateToString(newsEditContribution.getUpdateTime()));
                }
            }
        }
        List<NewsOperateLog> newsOperateLogs = newsLogLoader.loadByCId(newsContribution.getId());
        if (!CollectionUtils.isEmpty(newsOperateLogs)) {
            List<OperateLogDetail> list = new ArrayList<>();
            List<Long> operatorIds = newsOperateLogs.stream().map(NewsOperateLog::getOperateId).collect(Collectors.toList());
            Map<Long, NewsUserInfo> userInfosMap = newsUserInfoLoader.loadByIds(operatorIds).stream().collect(Collectors.toMap(NewsUserInfo::getId, Function.identity(), (l, r) -> r));
            for (NewsOperateLog log : newsOperateLogs) {
                OperateLogDetail logDetail = new OperateLogDetail();
                logDetail.setId(log.getId());
                logDetail.setOperateId(log.getOperateId());
                NewsUserInfo newsUserInfo = userInfosMap.get(log.getOperateId());
                if (Objects.nonNull(newsUserInfo)) {
                    logDetail.setOperateName(newsUserInfo.getUserName());
                }
                logDetail.setOperateTime(DateUtils.DateToString(log.getOperateTime()));
                logDetail.setStatus(log.getStatus());
                OperateLogBean jsonObject = JSONObject.toJavaObject(JSON.parseObject(log.getOperateDetail()), OperateLogBean.class);
                if (Objects.nonNull(jsonObject)) {
                    logDetail.setDocAuthor(jsonObject.getDocAuthor());
                    logDetail.setDocUrl(jsonObject.getDocUrl());
                    logDetail.setPicUrl(jsonObject.getPicUrl());
                    logDetail.setPicAuthor(jsonObject.getPicAuthor());
                    logDetail.setSuggestion(jsonObject.getSuggestion());
                }
                list.add(logDetail);
            }
            detail.setLogList(list);
        }
        return MapMessage.successMessage().add("data", detail);
    }

    //草稿编辑
    @RequestMapping(value = "edit.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage edit(@Validated @RequestBody ContributionEditParam param) {
        MapMessage mapMessage = checkContribution(param);
        if (!mapMessage.isSuccess()) {
            return mapMessage;
        }
        NewsContribution newsContribution = (NewsContribution) mapMessage.get("contribution");
        if (!newsContribution.getStatus().equals(ContributionStatus.DRAFT.name())) {
            return MapMessage.errorMessage().add("info", "当前稿件状态不是草稿状态，不能编辑");
        }
        BeanUtils.copyProperties(param, newsContribution);
        newsContribution.setSubmitTime(new Date());
        int result = newsWriterContributionService.update(newsContribution);
        if (result == 0) {
            return MapMessage.errorMessage().add("info", "编辑失败");
        }
        return MapMessage.successMessage();
    }

    //重投待审稿编辑
    @RequestMapping(value = "reSubmit.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage reSubmit(@Validated @RequestBody ContributionEditParam param) {
        MapMessage mapMessage = checkContribution(param);
        if (!mapMessage.isSuccess()) {
            return mapMessage;
        }
        NewsContribution newsContribution = (NewsContribution) mapMessage.get("contribution");
        if (!newsContribution.getStatus().equals(ContributionStatus.APPROVAL_REJECTION.name())) {
            return MapMessage.errorMessage().add("info", "当前稿件状态不是审稿不过待修改，不能编辑");
        }
        BeanUtils.copyProperties(param, newsContribution);
        newsContribution.setSubmitTime(new Date());
        newsContribution.setStatus(ContributionStatus.RE_APPROVAL_PENDING.name());
        int result = newsWriterContributionService.update(newsContribution);
        if (result == 0) {
            return MapMessage.errorMessage().add("info", "编辑失败");
        }
        newsLogService.createLog(OperateType.CONTRIBUTOR_EDIT.name(), param.getUserId(), newsContribution.getId(), newsContribution.getStatus(), newsContribution.getDocAuthor(), newsContribution.getDocUrl(), newsContribution.getPicAuthor(), newsContribution.getPicUrl(), null);
        return MapMessage.successMessage();
    }

    private MapMessage checkContribution(ContributionEditParam param) {
        if (param.getId() == null || param.getId() <= 0L) {
            return MapMessage.errorMessage().add("info", "稿件id不能为空");
        }
        NewsContribution newsContribution = newsWriterContributionLoader.selectById(param.getId());
        if (Objects.isNull(newsContribution)) {
            return MapMessage.errorMessage().add("info", "稿件id有误");
        }
        return MapMessage.successMessage().add("contribution", newsContribution);
    }

    @RequestMapping(value = "delete.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage delete(@Validated @NotNull @Min(value = 1, message = "id必须大于0") Long id) {
        NewsContribution newsContribution = newsWriterContributionLoader.selectById(id);
        if (Objects.isNull(newsContribution)) {
            return MapMessage.errorMessage().add("info", "id有误");
        }
        if (!newsContribution.getStatus().equals(ContributionStatus.DRAFT.name())) {
            return MapMessage.errorMessage().add("info", "当前稿件不是草稿状态，不能删除");
        }
        int delete = newsWriterContributionService.delete(id);
        if (delete == 0) {
            return MapMessage.errorMessage().add("info", "删除失败");
        }
        return MapMessage.successMessage();
    }

    @RequestMapping(value = "withDraw.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage withDraw(@Validated @NotNull @Min(value = 1, message = "id必须大于0") Long id) {
        NewsContribution newsContribution = newsWriterContributionLoader.selectById(id);
        if (Objects.isNull(newsContribution)) {
            return MapMessage.errorMessage().add("info", "id有误");
        }
        if (!newsContribution.getStatus().equals(ContributionStatus.APPROVAL_PENDING.name())) {
            return MapMessage.errorMessage().add("info", "当前稿件不是待审批状态，不能撤回");
        }
        newsContribution.setStatus(ContributionStatus.DRAFT.name());
        int delete = newsWriterContributionService.update(newsContribution);
        if (delete == 0) {
            return MapMessage.errorMessage().add("info", "撤回失败");
        }
        newsLogService.createLog(OperateType.CONTRIBUTOR_WITH_DRAW.name(), 31L, newsContribution.getId(), newsContribution.getStatus(), newsContribution.getDocAuthor(), newsContribution.getDocUrl(), newsContribution.getPicAuthor(), newsContribution.getPicUrl(), null);
        return MapMessage.successMessage();
    }

    @RequestMapping(value = "approve/list.vpage", method = RequestMethod.GET)
    @ResponseBody
    public MapMessage approveList(HttpServletRequest request) {
        String eno = request.getHeader("eno");
        NewsUserInfo newsUserInfo = newsUserInfoLoader.loadByEno(eno);
        if (Objects.isNull(newsUserInfo)) {
            return MapMessage.errorMessage().add("info", "用户信息有误");
        }
        UserReq req = new UserReq();
        req.setUnit(newsUserInfo.getUnit());
        req.setRoleType(UserRoleType.APPROVER.name());
        List<NewsUserInfo> list = newsUserInfoLoader.list(req);
        if (CollectionUtils.isEmpty(list)) {
            return MapMessage.successMessage().add("data", Collections.emptyList());
        }
        List<UserInfo> userInfoList = new ArrayList<>();
        for (NewsUserInfo user : list) {
            UserInfo info = new UserInfo();
            info.setId(user.getId());
            info.setName(user.getUserName());
            userInfoList.add(info);
        }
        return MapMessage.successMessage().add("data", userInfoList);
    }
}
