package com.example.hotelboard.service;

import com.example.hotelboard.dto.UsersForm;
import com.example.hotelboard.entity.Users;
import com.example.hotelboard.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean existsByUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public void signUp(UsersForm form) {
        Users users = form.toEntity();
        userRepository.save(users);
    }

    @Override
    public Users signIn(String userId, String password) {
        return userRepository.findByUserIdAndPassword(userId, password);
    }
}
