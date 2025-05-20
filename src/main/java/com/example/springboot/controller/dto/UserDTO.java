package com.example.springboot.controller.dto;

import com.example.springboot.entity.Menu;
import lombok.Data;

import java.util.List;


@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String avatarUrl;
    private String code;
    private String token;
    private String role;
    private List<Menu> menus;
    private String sex;

}
