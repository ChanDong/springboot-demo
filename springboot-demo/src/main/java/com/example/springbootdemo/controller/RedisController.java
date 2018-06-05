package com.example.springbootdemo.controller;

import com.example.springbootdemo.domain.Billboard;
import com.example.springbootdemo.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by shixi03 on 2018/6/5.
 */
@Controller
public class RedisController {

    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    private RedisService redisService;

    @RequestMapping("/redisZsetPutTest")
    public String redisZsetPutTest(Model model) {
        List<Billboard> billboards =  redisService.redisZsetPut();
        model.addAttribute("billboards", billboards);
        return "redisZsetList";
    }
}
