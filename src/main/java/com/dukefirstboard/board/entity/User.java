package com.dukefirstboard.board.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String role;
    private boolean enabled;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}