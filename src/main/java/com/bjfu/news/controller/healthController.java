package com.bjfu.news.controller;

import com.bjfu.news.untils.MapMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class healthController {
    @RequestMapping(value = "health", method = RequestMethod.GET)
    @ResponseBody
    public MapMessage health() {
        return MapMessage.successMessage().add("v", 1.0);
    }
}
