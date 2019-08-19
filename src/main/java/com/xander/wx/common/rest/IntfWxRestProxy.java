package com.xander.wx.common.rest;

/**
 * Description: 微信 网络请求代理 接口
 *
 * @author Xander
 * datetime: 2019/8/19 10:57
 */
public interface IntfWxRestProxy {

    /**
     * POST 请求
     *
     * @param url       请求 url
     * @param postParam 表单参数
     * @param clazz     Class
     * @param <T>
     * @return
     */
    <T> T post(String url, Object postParam, Class<T> clazz);

    /**
     * GET 请求
     *
     * @param url   请求 url
     * @param clazz Class
     * @param <T>
     * @return
     */
    <T> T get(String url, Class<T> clazz);
}
