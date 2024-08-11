package com.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 标签表
 *
 * @TableName tag
 */
@Data

@TableName(value = "tag")
public class Tag implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标签名称
     */
    private String tag_name;

    /**
     * 用户id
     */
    private Integer user_id;

    /**
     * 0-不是，1-是
     */
    private Integer parent_id;

    /**
     * 是否父标签
     */
    private Integer is_parent;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer is_delete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}