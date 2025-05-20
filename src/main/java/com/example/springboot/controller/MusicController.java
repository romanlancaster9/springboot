package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.service.ITypeService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;
import com.example.springboot.service.IUserService;

import com.example.springboot.service.IMusicService;
import com.example.springboot.entity.Music;

import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/music")
public class MusicController {

    @Resource
    private IMusicService musicService;
    @Resource
    IUserService userService;

    @Resource
    private ITypeService typeService;


    private final String now = DateUtil.now();

    @GetMapping("/time")
    public Result time() {
        return Result.success(musicService.list(new LambdaQueryWrapper<Music>().orderByDesc(Music::getId)).stream().limit(4).collect(Collectors.toList()));
    }

    @GetMapping("/page/type")
    public Result findPage(@RequestParam Integer typeId,
                           @RequestParam("") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
//        queryWrapper.eq("state","pass");
        if (0 != typeId) {
            queryWrapper.eq("type_id", typeId);
        }

        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }

        Page<Music> page = musicService.page(new Page<>(pageNum, pageSize), queryWrapper);
        page.getRecords().stream().forEach(music -> {
            music.setTypeName(typeService.getById(music.getTypeId()).getName());
        });
        return Result.success(page);
    }


    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Music music) {
        if (music.getId() == null) {
            //music.setTime(DateUtil.now());
            //music.setUser(TokenUtils.getCurrentUser().getNickname());
            music.setUserId(TokenUtils.getCurrentUser().getId());
        }
        musicService.saveOrUpdate(music);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        musicService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        musicService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(musicService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        Music music = musicService.getById(id);
        music.setTypeName(typeService.getById(music.getTypeId()).getName());
        return Result.success(music);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Music> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }

        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser.getRole().equals("ROLE_USER")) {
            queryWrapper.eq("user_id", currentUser.getId());
//            queryWrapper.eq("state","pass");
        }

        Page<Music> page = musicService.page(new Page<>(pageNum, pageSize), queryWrapper);
        page.getRecords().stream().forEach(music -> {
            if (music.getTypeId() != null) {
                music.setTypeName(typeService.getById(music.getTypeId()).getName());
            }
        });
        return Result.success(page);
    }

}

