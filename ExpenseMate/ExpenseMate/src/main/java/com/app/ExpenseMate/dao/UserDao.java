package com.app.ExpenseMate.dao;

import com.app.ExpenseMate.entity.User;

import java.util.List;

public interface UserDao {
    public List<User> getAllUsers();
    public User getUserByPhoneNumber(String phoneNumber);
    public User getUserByEmail(String email);
}
