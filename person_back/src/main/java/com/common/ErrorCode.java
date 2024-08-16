package com.common;

/**
 * 错误类
 *
 * @author lack
 */
public enum ErrorCode {

    SUCCESS(0, "OK", ""),

    PARAMS_ERROR(4000,"请求数据错误", ""),

    NULL_ERROR(4001, "请求数据为空", ""),

    NO_LOGIN(40100, "未登录", ""),

    NO_AUTH(40101, "无权限", ""),

    SYSTEM_ERROR(5000, "系统异常", "");

    /**
     * 状态码
     */
    private final int code;


    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
