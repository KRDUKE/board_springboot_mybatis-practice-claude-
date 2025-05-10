package com.dukefirstboard.board.service;


import com.dukefirstboard.board.entity.User;
import com.dukefirstboard.board.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void register(User user) {
        // 패스워드 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 기본 권한 설정
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        userMapper.insertUser(user);
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public boolean checkUsernameDuplicate(String username) {
        return userMapper.findByUsername(username) != null;
    }

    public boolean checkEmailDuplicate(String email) {
        return userMapper.findByEmail(email) != null;
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updatePassword(user);
    }

    public void deleteUser(Long userId) {
        userMapper.deleteUser(userId);
    }
}