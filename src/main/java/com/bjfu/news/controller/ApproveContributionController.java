package com.bjfu.news.controller;

import com.bjfu.news.constant.ApproveStatus;
import com.bjfu.news.constant.ContributionStatus;
import com.bjfu.news.constant.OperateType;
import com.bjfu.news.entity.NewsApproveContribution;
import com.bjfu.news.entity.NewsContribution;
import com.bjfu.news.entity.NewsUserInfo;
import com.bjfu.news.req.ContributionReq;
import com.bjfu.news.untils.MapMessage;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/approve")
@CrossOrigin
public class ApproveContributionController extends AbstractNewsController {

    @RequestMapping(value = "list.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage list(@Validated @RequestBody ContributionReq req, HttpServletRequest request) {
        Map<String, Object> map = getInitialMap();
        String eno = request.getHeader("userId");
        NewsUserInfo newsUserInfo = newsUserInfoLoader.loadByEno(eno);
        if (Objects.isNull(newsUserInfo)) {
            return MapMessage.errorMessage().add("info", "用户信息有误");
        }
        req.setUserId(newsUserInfo.getId());
        List<NewsApproveContribution> contributions = approveContributionLoader.selectByApproveId(req.getUserId());
        if (CollectionUtils.isEmpty(contributions)) {
            return MapMessage.successMessage().add("data", map);
        }
        List<Long> contributionIds = contributions.stream().map(NewsApproveContribution::getContributionId).collect(Collectors.toList());
        req.setContributionIds(contributionIds);
        if (req.getStatus() == null || StringUtils.isEmpty(req.getStatus())) {
            req.setStatusList(ContributionStatus.APPROVE_MAPPING.keySet());
        }
        req.setUserId(null);
        return list(req, map);
    }

    //审批
    @RequestMapping(value = "approve.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage approve(@Validated @NotNull @Min(value = 1, message = "id必须大于0") Long id,
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
        NewsApproveContribution approveContribution = approveContributionLoader.selectByCId(id);
        if (Objects.isNull(approveContribution)) {
            return MapMessage.errorMessage().add("info", "稿件id有误");
        }
        approveContribution.setSuggestion(suggestion);
        approveContribution.setApproveTime(new Date());
        approveContributionService.update(approveContribution);
        if (status.equals(ApproveStatus.AGREE.name())) {
            contribution.setStatus(ContributionStatus.APPROVE.name());
        }
        if (status.equals(ApproveStatus.REJECTION.name())) {
            contribution.setStatus(ContributionStatus.APPROVAL_REJECTION.name());
        }
        newsWriterContributionService.update(contribution);
        newsLogService.createLog(OperateType.APPROVE_SUBMIT.name(), newsUserInfo.getId(), contribution.getId(), contribution.getStatus(), contribution.getDocAuthor(), contribution.getDocUrl(), contribution.getPicAuthor(), contribution.getPicUrl(), suggestion);
        return MapMessage.successMessage();
    }

    @RequestMapping(value = "withDraw.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage withDraw(@Validated @NotNull @Min(value = 1, message = "id必须大于0") Long id, HttpServletRequest request) {
        String eno = request.getHeader("userId");
        NewsUserInfo newsUserInfo = newsUserInfoLoader.loadByEno(eno);
        if (Objects.isNull(newsUserInfo)) {
            return MapMessage.errorMessage().add("info", "用户信息有误");
        }
        NewsContribution newsContribution = newsWriterContributionLoader.selectById(id);
        if (Objects.isNull(newsContribution)) {
            return MapMessage.errorMessage().add("info", "id有误");
        }
        if (!newsContribution.getStatus().equals(ContributionStatus.APPROVE.name())) {
            return MapMessage.errorMessage().add("info", "当前稿件不是审稿通过待编辑部处理，不能撤回");
        }
        NewsApproveContribution approveContribution = approveContributionLoader.selectByCId(id);
        if (Objects.isNull(approveContribution)) {
            return MapMessage.errorMessage().add("info", "稿件id有误");
        }
        newsContribution.setStatus(ContributionStatus.APPROVAL_PENDING.name());
        int delete = newsWriterContributionService.update(newsContribution);
        if (delete == 0) {
            return MapMessage.errorMessage().add("info", "撤回失败");
        }
        approveContribution.setSuggestion("");
        approveContribution.setApproveTime(null);
        approveContributionService.update(approveContribution);
        newsLogService.createLog(OperateType.APPROVE_WITH_DRAW.name(), newsUserInfo.getId(), newsContribution.getId(), newsContribution.getStatus(), newsContribution.getDocAuthor(), newsContribution.getDocUrl(), newsContribution.getPicAuthor(), newsContribution.getPicUrl(), null);
        return MapMessage.successMessage();
    }

    @RequestMapping(value = "suggestion.vpage", method = RequestMethod.GET)
    @ResponseBody
    public MapMessage suggestion(@Validated @NotNull @Min(value = 1, message = "id必须大于0") Long id) {
        NewsApproveContribution approveContribution = approveContributionLoader.selectByCId(id);
        if (Objects.isNull(approveContribution)) {
            return MapMessage.errorMessage().add("info", "稿件id有误");
        }
        return MapMessage.successMessage().add("data", approveContribution.getSuggestion());
    }

    @RequestMapping(value = "addSuggestion.vpage", method = RequestMethod.POST)
    @ResponseBody
    public MapMessage addSuggestion(@Validated @NotNull @Min(value = 1, message = "id必须大于0") Long id, @Validated @NotBlank String suggestion) {
        NewsApproveContribution approveContribution = approveContributionLoader.selectByCId(id);
        if (Objects.isNull(approveContribution)) {
            return MapMessage.errorMessage().add("info", "稿件id有误");
        }
        if (StringUtils.isEmpty(suggestion)) {
            return MapMessage.errorMessage().add("info", "建议不能为空");
        }
        approveContribution.setSuggestion(suggestion);
        approveContributionService.update(approveContribution);
        return MapMessage.successMessage();
    }


}
