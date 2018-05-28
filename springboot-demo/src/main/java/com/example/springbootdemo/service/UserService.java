package com.example.springbootdemo.service;

import com.example.springbootdemo.dao.UserDAO;
import com.example.springbootdemo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shixi03 on 2018/5/23.
 */
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    public List<User> getAllUser() {
        return userDAO.getAllUser();
    }
}
