package com.bjfu.news.service.impl;

import com.bjfu.news.constant.ContributionStatus;
import com.bjfu.news.constant.OperateType;
import com.bjfu.news.dao.NewsApproveContributionMapper;
import com.bjfu.news.dao.NewsContributionMapper;
import com.bjfu.news.entity.NewsApproveContribution;
import com.bjfu.news.entity.NewsContribution;
import com.bjfu.news.req.ContributionCreateParam;
import com.bjfu.news.service.NewsLogService;
import com.bjfu.news.service.NewsWriterContributionService;
import com.bjfu.news.untils.MapMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class NewsWriterContributionServiceImpl implements NewsWriterContributionService {

    @Autowired
    private NewsContributionMapper newsContributionMapper;

    @Autowired
    private NewsApproveContributionServiceImpl newsApproveContributionService;

    @Autowired
    private NewsLogService newsLogService;

    @Autowired
    private NewsApproveContributionMapper newsApproveContributionMapper;

    @Override
    public int delete(Long id) {
        try {
            return newsContributionMapper.delete(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public int update(NewsContribution newsContribution) {
        try {
            return newsContributionMapper.update(newsContribution);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public NewsContribution create(NewsContribution contribution) {
        try {
            newsContributionMapper.insert(contribution);
            return contribution;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    @Transactional
    public MapMessage submitContribution(ContributionCreateParam param) {
        NewsContribution contribution = new NewsContribution();
        BeanUtils.copyProperties(param, contribution);
        contribution.setSubmitTime(new Date());
        contribution.setStatus(ContributionStatus.APPROVAL_PENDING.name());
        contribution.setDisabled(false);
        NewsContribution newsContribution = create(contribution);
        if (newsContribution == null) {
            return MapMessage.errorMessage().add("info", "提交稿件失败");
        }
        NewsApproveContribution approveContribution = new NewsApproveContribution();
        approveContribution.setContributionId(newsContribution.getId());
        approveContribution.setDisabled(false);
        approveContribution.setUserId(param.getApproveId());
        NewsApproveContribution approveContribution1 = newsApproveContributionService.create(approveContribution);
        if (approveContribution1 == null) {
            return MapMessage.errorMessage().add("info", "提交稿件失败");
        }
        newsLogService.createLog(OperateType.CONTRIBUTOR_SUBMIT.name(), 31L, contribution.getId(), contribution.getStatus(), contribution.getDocAuthor(), contribution.getDocUrl(), contribution.getPicAuthor(), contribution.getPicUrl(), null);
        return MapMessage.successMessage();
    }

    @Override
    @Transactional
    public MapMessage fastSubmit(Long id, Long approveId) {
        NewsContribution contribution = newsContributionMapper.selectById(id);
        if (contribution == null) {
            return MapMessage.errorMessage().add("info", "提交稿件失败");
        }
        contribution.setStatus(ContributionStatus.APPROVAL_PENDING.name());
        contribution.setSubmitTime(new Date());
        update(contribution);
        NewsApproveContribution approveContribution = newsApproveContributionMapper.selectByCId(contribution.getId());
        if (Objects.nonNull(approveContribution)) {
            approveContribution.setUserId(approveId);
            newsApproveContributionService.update(approveContribution);
        } else {
            approveContribution = new NewsApproveContribution();
            approveContribution.setContributionId(contribution.getId());
            approveContribution.setDisabled(false);
            approveContribution.setUserId(approveId);
            newsApproveContributionService.create(approveContribution);
        }
        newsLogService.createLog(OperateType.CONTRIBUTOR_SUBMIT.name(), 31L, contribution.getId(), contribution.getStatus(), contribution.getDocAuthor(), contribution.getDocUrl(), contribution.getPicAuthor(), contribution.getPicUrl(), null);
        return MapMessage.successMessage();
    }

    @Override
    public MapMessage saveDraft(ContributionCreateParam param) {
        NewsContribution contribution = new NewsContribution();
        BeanUtils.copyProperties(param, contribution);
        contribution.setStatus(ContributionStatus.DRAFT.name());
        contribution.setDisabled(false);
        contribution.setSubmitTime(new Date());
        NewsContribution newsContribution = create(contribution);
        if (newsContribution == null) {
            return MapMessage.errorMessage().add("info", "存为草稿失败");
        }
        return MapMessage.successMessage();
    }
}
