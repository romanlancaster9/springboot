package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.service.IMusicService;
import com.example.springboot.service.ITypeService;
import com.example.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IMusicService musicService;

    @Autowired
    private ITypeService typeService;

    @GetMapping("/count")
    public Result count() {
        long userCount = userService.count(new LambdaQueryWrapper<User>().ne(User::getUsername, "admin"));
        long musicCount = musicService.count();
        long typeCount = typeService.count();

        Map<String, Long> result = new LinkedHashMap<>();
        result.put("userCount",userCount);
        result.put("musicCount",musicCount);
        result.put("typeCount",typeCount);
        return Result.success(result);
    }

    @GetMapping("/user")
    public Result user() {


        long boy = userService.count(new LambdaQueryWrapper<User>().eq(User::getSex, "man"));
        long girl = userService.count(new LambdaQueryWrapper<User>().eq(User::getSex, "woman"));

        List<Map<String,Object>> result = new ArrayList<>();

        Map<String, Object> map1 = new LinkedHashMap<>();
        map1.put("name","man");
        map1.put("value",boy);
        result.add(map1);

        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put("name","woman");
        map2.put("value",girl);
        result.add(map2);

        return Result.success(result);
    }
}
