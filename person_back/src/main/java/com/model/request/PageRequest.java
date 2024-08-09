package com.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageRequest implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private int pageNum = 1;
    /**
     * 页面大小
     */
    private int pageSize = 10;
}
