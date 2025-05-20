package com.example.springboot.controller.dto;

import com.example.springboot.entity.Music;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Accessors(chain = true)
public class PlaylistMusicDTO {

    private List<Music> playlistMusics;

    private Integer playlistId;

}
