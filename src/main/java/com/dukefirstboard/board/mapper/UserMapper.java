package com.dukefirstboard.board.mapper;

import com.dukefirstboard.board.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertUser(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    void updateUser(User user);
    void updatePassword(User user);
    void deleteUser(Long userId);
}