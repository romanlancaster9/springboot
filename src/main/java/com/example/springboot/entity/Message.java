package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import cn.hutool.core.annotation.Alias;


@Getter
@Setter
@ApiModel(value = "Message对象", description = "")
public class Message implements Serializable {

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("反馈内容")
    private String content;

    @ApiModelProperty("反馈类别")
    private String type;

    @ApiModelProperty("反馈图片")
    @Alias("反馈图片")
    private String img;

    @ApiModelProperty("用户")
    private Integer userId;

    @ApiModelProperty("时间")
    private String time;

    @TableField(exist = false)
    private User user;


}
