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
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by shixi03 on 2018/6/5.
 */
@Controller
public class FileController {

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

    @RequestMapping("/multifileUpload")
    public String uploadFile(Model model, MultipartFile filename, HttpServletRequest request) {
        if (filename.isEmpty()) {
            model.addAttribute("message", "上传文件失败");
            return "login";
        }
        String fileName = filename.getOriginalFilename();

        String path = request.getSession().getServletContext().getRealPath("/") + "excels\\";
        File dest = new File(path + "/" + fileName);
        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            filename.transferTo(dest);
            model.addAttribute("message", "上传文件成功");
            return "login";
        } catch (IllegalStateException e) {
            e.printStackTrace();
            model.addAttribute("message", "上传文件失败");
            return "login";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "上传文件失败!");
            return "login";
        }
    }

    @RequestMapping("/multifileDownLoad")
    public String downLoad(Model model, MultipartFile multipartFile, HttpServletResponse response) {
//        String filename = multipartFile.getOriginalFilename();
        String filePath = "E:/test/1.png";
//        File file = new File(filePath + "/" + filename);
        File file = new File(filePath);
        if (file.exists()) {
            response.setHeader("Content-Disposition", "attachment;fileName=" + filePath);
            byte[] buffer = new byte[1024];

            FileInputStream fis = null;
            BufferedInputStream bis = null;

            OutputStream os = null;
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
                bis.close();
                fis.close();
                model.addAttribute("message", "下载成功");
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "下载失败");
            }

        }
        return "login";
    }
}
