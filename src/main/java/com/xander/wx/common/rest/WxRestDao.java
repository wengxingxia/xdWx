package com.xander.wx.common.rest;


import com.xander.wx.common.utils.StringFormatUtil;

/**
 * Description: 微信网络请求 dao
 *
 * @author Xander
 * datetime: 2019/8/19 11:23
 */
public abstract class WxRestDao {

    /**
     * 通过 IntfWxRestProxy 发送网络请求
     *
     * @return
     */
    public IntfWxRestProxy getRestProxy() {
        return WxRestProxyImpl.getInstance();
    }

    /**
     * 获取 服务主机，例如：https://api.weixin.qq.com
     *
     * @return
     */
    public abstract String getService();

    /**
     * POST 请求
     *
     * @param uri       请求 uri
     * @param postParam 表单参数
     * @param clazz     Class
     * @param <T>
     * @return
     */
    public <T> T post(String uri, Object postParam, Class<T> clazz) {
        final String uriPattern = "{}/{}";// url 拼接规则 service + uri
        String url = StringFormatUtil.format(uriPattern, getService(), uri);
        T t = this.getRestProxy().post(url, postParam, clazz);
        return t;
    }

    /**
     * GET 请求
     *
     * @param uri   请求 uri
     * @param clazz Class
     * @param <T>
     * @return
     */
    public <T> T get(String uri, Class<T> clazz) {
        final String uriPattern = "{}/{}";// url 拼接规则 service + uri
        String url = StringFormatUtil.format(uriPattern, getService(), uri);
        T t = this.getRestProxy().get(url, clazz);
        return t;
    }
}