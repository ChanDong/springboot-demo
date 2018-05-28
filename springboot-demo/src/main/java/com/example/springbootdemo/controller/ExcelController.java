package com.example.springbootdemo.controller;

import com.example.springbootdemo.domain.User;
import com.example.springbootdemo.service.ExcelService;
import com.example.springbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by shixi03 on 2018/5/28.
 */
@Controller
public class ExcelController {

    @Autowired
    private ExcelService excelService;
    @Autowired
    private UserService userService;

    @RequestMapping("/userExcel")
    public String userExcel(Model model, MultipartFile userExcel, HttpServletRequest request) {
        excelService.userListByExcel(userExcel, request);
        List<User> userList = userService.getAllUser();
        model.addAttribute("userList", userList);
        return "userList";
    }
}
