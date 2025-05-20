package com.example.springboot.controller.dto;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class MusicDTO {

    private String title;
    private String artist;
    private String url;
    private String pic;
    private String lrc;
    private String theme;

}
