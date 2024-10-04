package com.example.hotelboard.service;

import com.example.hotelboard.dto.UsersForm;
import com.example.hotelboard.entity.Users;

public interface UserService {
    boolean existsByUserId(String userId);
    void signUp(UsersForm form);
    Users signIn(String userId, String password);
}
