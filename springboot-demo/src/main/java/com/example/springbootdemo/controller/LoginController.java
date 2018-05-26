package com.example.springbootdemo.controller;

import com.example.springbootdemo.domain.User;
import com.example.springbootdemo.service.UserService;
import com.example.springbootdemo.service.redis.RedisService;
import com.example.springbootdemo.utils.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("message", "");
        redisService.redisStringPut();
        User user = userService.getUserById(1);
        logger.info("User:{}", user.toString());
        return "login";
    }

    @RequestMapping("/checkPhone")
    public String checkPhone(Model model, String phone) {
        Boolean isPhone = RegexUtil.isPhone(phone);
        if (isPhone) {
            model.addAttribute("message", "手机正确");
        } else {
            model.addAttribute("message", "手机错误");
        }
        return "login";
    }
}
