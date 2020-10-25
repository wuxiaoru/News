package com.bjfu.news.controller;

import com.bjfu.news.constant.ContributionStatus;
import com.bjfu.news.constant.UserRoleType;
import com.bjfu.news.untils.MapMessage;
import com.neusoft.education.tp.sso.client.filter.CASFilterRequestWrapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.context.support.UiApplicationContextUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author anne
 */
@Controller
@RequestMapping("/v1/news")
@CrossOrigin(origins = "*")
public class IndexController {

    @RequestMapping(value = "/enum.vpage", method = RequestMethod.GET)
    @ResponseBody
    public MapMessage enumList(HttpServletRequest request) {
        CASFilterRequestWrapper reqWrapper = new CASFilterRequestWrapper(request);
        String userID = reqWrapper.getRemoteUser();
        return MapMessage.successMessage().add("status", ContributionStatus.CODE_MAPPING)
                .add("role", UserRoleType.NAME_MAPPING)
                .add("approveStatus", ContributionStatus.APPROVE_MAPPING)
                .add("editStatus", ContributionStatus.EDITOR_MAPPING);
    }

    @RequestMapping("/index")
    public String test(){
        return "index";
    }

}
