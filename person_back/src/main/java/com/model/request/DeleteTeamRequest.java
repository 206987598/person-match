package com.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 解散队伍封装体
 *
 * @TableName team
 */

@Data
public class DeleteTeamRequest implements Serializable {
    /**
     * 队伍id
     */
    private long id;


    private static final long serialVersionUID = 1L;
}