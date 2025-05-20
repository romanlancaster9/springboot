package com.example.springboot.service.impl;

import com.example.springboot.entity.Music;
import com.example.springboot.mapper.MusicMapper;
import com.example.springboot.service.IMusicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music> implements IMusicService {

}
