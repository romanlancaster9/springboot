package com.example.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.config.interceptor.AuthAccess;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.controller.dto.UserPasswordDTO;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.IUserService;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.utils.EmailUtils;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Autowired
    private EmailUtils emailUtils;

    @PostMapping("/loginSms")
    public Result loginSms(@RequestBody UserDTO userDTO) {
        String email = userDTO.getEmail();
        if (StrUtil.isBlank(email)) {
            return Result.error(Constants.CODE_400, "email is not null");
        }
        LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>().lambda().eq(User::getEmail, email);
        List<User> list = userService.list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return Result.error(Constants.CODE_600, "email is not exist");
        }
        int randomNumber = (int)(Math.random() * 900000) + 100000;
//        String randomNumber = "000000";
        emailUtils.sendEmail(email, "Account Login", "Hello, Welcome to log in. The verification code is ：" + randomNumber + "，the validity period is 5 minutes",false);
        System.out.println(email + " 的登录验证码为： " + randomNumber);

        String codeToken = TokenUtils.getCodeToken(String.valueOf(randomNumber), email);

//        String code = TokenUtils.getCode(codeToken);
        return Result.success(codeToken);
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        String codeToken = userDTO.getToken();
        if (StrUtil.isBlank(codeToken)) {
            return Result.error(Constants.CODE_400, "please send email code");
        }


        String loginToken = TokenUtils.getCode(codeToken);

        if (StrUtil.isBlank(loginToken) || StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "parameter error");
        }

        if (!loginToken.equals(userDTO.getCode())) {
            return Result.error(Constants.CODE_400, "Invalid code");
        }

        UserDTO dto = userService.login(userDTO);
        return Result.success(dto);
    }

    @PostMapping("/registerSms")
    public Result registerSms(@RequestBody UserDTO userDTO) {
        String email = userDTO.getEmail();
        if (StrUtil.isBlank(email)) {
            return Result.error(Constants.CODE_400, "email is not null");
        }
        LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>().lambda().eq(User::getEmail, email);
        List<User> list = userService.list(wrapper);
        if (!CollUtil.isEmpty(list)) {
            return Result.error(Constants.CODE_600, "email is exist");
        }
        int randomNumber = (int)(Math.random() * 900000) + 100000;

        emailUtils.sendEmail(email, "Account Register", "Hello, The registration process is currently underway. The verification code is ：" + randomNumber + "，the validity period is 5 minutes",false);
        System.out.println(email + " 的注册验证码为： " + randomNumber);

        String codeToken = TokenUtils.getCodeToken(String.valueOf(randomNumber), email);

//        String code = TokenUtils.getCode(codeToken);
        return Result.success(codeToken);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        String codeToken = userDTO.getToken();

        if (StrUtil.isBlank(codeToken)) {
            return Result.error(Constants.CODE_400, "please send email code");
        }

        String registerToken = TokenUtils.getCode(codeToken);

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password) || StrUtil.isBlank(registerToken)) {
            return Result.error(Constants.CODE_400, "parameter error");
        }

        if (!registerToken.equals(userDTO.getCode())) {
            return Result.error(Constants.CODE_400, "Invalid code");
        }

        userDTO.setNickname(userDTO.getUsername());
        return Result.success(userService.register(userDTO));
    }

     // 新增或者更新
    @PostMapping
    public Result save(@RequestBody User user) {
        String username = user.getUsername();
        if (StrUtil.isBlank(username)) {
            return Result.error(Constants.CODE_400, "parameter error");
        }
        if (StrUtil.isBlank(user.getNickname())) {
            user.setNickname(username);
        }
        if (user.getId() != null) {
            user.setPassword(null);
        } else {
            if (user.getPassword() == null) {
                user.setPassword("202cb962ac59075b964b07152d234b70");
            }
        }
        return Result.success(userService.saveOrUpdate(user));
    }

    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.updatePassword(userPasswordDTO);
        return Result.success();
    }

    @AuthAccess
    @PutMapping("/reset")
    public Result reset(@RequestBody UserPasswordDTO userPasswordDTO) {
        if (StrUtil.isBlank(userPasswordDTO.getUsername()) || StrUtil.isBlank(userPasswordDTO.getPhone())) {
            throw new ServiceException("-1", "params err");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userPasswordDTO.getUsername());
        queryWrapper.eq("phone", userPasswordDTO.getPhone());
        List<User> list = userService.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            User user = list.get(0);
            user.setPassword("123");
            userService.updateById(user);
        } else {
            return Result.error("-1", "未找到用户");
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

