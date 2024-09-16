
/**
 * RestBean类是一个通用的响应包装类，用于封装HTTP请求的响应数据。
 *
 * @param <T> 响应数据的类型
 * 
 * 属性:
 * - status: 响应状态码
 * - success: 响应是否成功
 * - message: 响应数据
 * 
 * 构造方法:
 * - RestBean(int status, boolean success, T message): 初始化RestBean对象
 * 
 * 静态方法:
 * - <T> RestBean<T> success(): 返回一个状态码为200且无数据的成功响应
 * - <T> RestBean<T> success(T data): 返回一个状态码为200且包含数据的成功响应
 * - <T> RestBean<T> failure(): 返回一个状态码为500且无数据的失败响应
 * - <T> RestBean<T> failure(int status, T data): 返回一个指定状态码且包含数据的失败响应
 */
package com.study.vue.entity;

import lombok.Data;

@Data
public class RestBean<T> {
    private int status;
    private boolean success;
    private T message;

    public RestBean(int status, boolean success, T message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

    public static <T> RestBean<T> success() {
        return new RestBean<T>(200, true, null);
    }

    public static <T> RestBean<T> success(T data) {
        return new RestBean<T>(200, true, data);
    }

    public static <T> RestBean<T> failure() {
        return new RestBean<T>(500, false, null);
    }

    public static <T> RestBean<T> failure(int status,T data) {
        return new RestBean<T>(status, false, data);
    }
}
