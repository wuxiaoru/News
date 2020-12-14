package com.bjfu.news.controller;

import com.bjfu.news.constant.ContributionStatus;
import com.bjfu.news.constant.EditStatus;
import com.bjfu.news.constant.OperateType;
import com.bjfu.news.entity.NewsApproveContribution;
import com.bjfu.news.entity.NewsContribution;
import com.bjfu.news.entity.NewsEditContribution;
import com.bjfu.news.entity.NewsUserInfo;
import com.bjfu.news.model.EditorContributionList;
import com.bjfu.news.req.ContributionReq;
import com.bjfu.news.req.UserReq;
import com.bjfu.news.untils.DateUtils;
import com.bjfu.news.untils.MapMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/v1/edit")
@CrossOrigin
public class EditContributionController extends AbstractNewsController {

    @RequestMapping(value = "list.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage list(@Validated @RequestBody ContributionReq req) {
        Map<String, Object> map = getInitialMap();
        if (req.getStartTime() != null && req.getEndTime() != null) {
            List<NewsApproveContribution> contributions = approveContributionLoader.selectByDate(req.getStartTime(), req.getEndTime());
            if (CollectionUtils.isEmpty(contributions)) {
                return MapMessage.successMessage().add("data", map);
            }
            List<Long> contributionIds = contributions.stream().map(NewsApproveContribution::getContributionId).collect(Collectors.toList());
            req.setContributionIds(contributionIds);
        }
        req.setStart(null);
        req.setEndTime(null);
        if (req.getUnit() != null && !StringUtils.isEmpty(req.getUnit())) {
            UserReq req1 = new UserReq();
            req1.setUnit(req.getUnit());
            List<NewsUserInfo> list = newsUserInfoLoader.list(req1);
            if (CollectionUtils.isEmpty(list)) {
                return MapMessage.successMessage().add("data", map);
            }
            List<Long> userIds = list.stream().map(NewsUserInfo::getId).collect(Collectors.toList());
            req.setUserIds(userIds);
        }
        int size = req.getSize() != null ? req.getSize() : 10;
        int page = req.getPage() != null ? req.getPage() : 0;
        req.setStart((page - 1) * size > 0 ? (page - 1) * size : 0);
        req.setSize(size);
        if (req.getStatus() == null || StringUtils.isEmpty(req.getStatus())) {
            req.setStatusList(ContributionStatus.EDITOR_MAPPING.keySet());
        }
        int count = newsWriterContributionLoader.getCount(req);
        List<NewsContribution> writerContributions = newsWriterContributionLoader.page(req);
        List<Long> cIds = writerContributions.stream().map(NewsContribution::getId).collect(Collectors.toList());
        List<NewsApproveContribution> approveContributions = approveContributionLoader.selectByCIds(cIds);
        List<Long> approveIds = approveContributions.stream().map(NewsApproveContribution::getUserId).collect(Collectors.toList());
        Map<Long, NewsUserInfo> userMap = newsUserInfoLoader.loadByIds(approveIds).stream().collect(Collectors.toMap(NewsUserInfo::getId, Function.identity()));
        Map<Long, NewsApproveContribution> approveContributionMap = approveContributions.stream().collect(Collectors.toMap(NewsApproveContribution::getContributionId, Function.identity()));
        List<EditorContributionList> list = new ArrayList<>();
        for (NewsContribution contribution : writerContributions) {
            EditorContributionList contributionList = new EditorContributionList();
            BeanUtils.copyProperties(contribution, contributionList);
            NewsApproveContribution approveContribution = approveContributionMap.get(contribution.getId());
            if (Objects.nonNull(approveContribution)) {
                if (approveContribution.getApproveTime() != null) {
                    contributionList.setApproveTime(DateUtils.DateToString(approveContribution.getApproveTime()));
                }
                NewsUserInfo newsUserInfo = userMap.get(approveContribution.getUserId());
                if (Objects.nonNull(newsUserInfo)) {
                    contributionList.setApproveName(newsUserInfo.getUserName());
                    contributionList.setUnit(newsUserInfo.getUnit());
                }
            }
            NewsUserInfo userInfo = newsUserInfoLoader.loadById(contribution.getUserId());
            if (Objects.nonNull(userInfo)) {
                contributionList.setUserName(userInfo.getUserName());
            }
            list.add(contributionList);
        }
        Map<String, Object> finalMap = getFinalMap(count, size, page, list, map);
        return MapMessage.successMessage().add("data", finalMap);
    }

    //审批
    @RequestMapping(value = "deal.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage deal(@Validated @NotNull @Min(value = 1, message = "id必须大于0") Long id,
                           @Validated @NotBlank(message = "状态不能为空") String status,
                           @RequestParam(required = false) String suggestion, HttpServletRequest request) {
        String eno = request.getHeader("userId");
        NewsUserInfo newsUserInfo = newsUserInfoLoader.loadByEno(eno);
        if (Objects.isNull(newsUserInfo)) {
            return MapMessage.errorMessage().add("info", "用户信息有误");
        }
        NewsContribution contribution = newsWriterContributionLoader.selectById(id);
        if (Objects.isNull(contribution)) {
            return MapMessage.errorMessage().add("info", "稿件id有误");
        }
        NewsEditContribution editContribution = new NewsEditContribution();
        if (StringUtils.isEmpty(suggestion)) {
            suggestion = "稿件已录用";
        }
        editContribution.setSuggestion(suggestion);
        editContribution.setContributionId(contribution.getId());
        editContribution.setDisabled(false);
        editContributionService.create(editContribution);
        if (status.equals(EditStatus.AGREE.name())) {
            contribution.setStatus(ContributionStatus.HIRE.name());
        }
        if (status.equals(EditStatus.REJECTION.name())) {
            contribution.setStatus(ContributionStatus.REJECTION.name());
        }
        newsWriterContributionService.update(contribution);
        newsLogService.createLog(OperateType.EDITOR_SUBMIT.name(), newsUserInfo.getId(), contribution.getId(), contribution.getStatus(), contribution.getDocAuthor(), contribution.getDocUrl(), contribution.getPicAuthor(), contribution.getPicUrl(), suggestion);
        return MapMessage.successMessage();
    }

}
