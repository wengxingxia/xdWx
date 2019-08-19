package com.xander.wx.common.rest;

import com.xander.wx.common.hotSwap.HotSwap;
import org.springframework.web.client.RestTemplate;

/**
 * Description: Rest 管理类
 * 使用该类的时候要先调用init方法进行初始化.
 *
 * @author Xander
 * datetime: 2019/8/19 11:01
 */
public class RestMgr {

    private RestTemplate restTemplate;

    public static RestMgr getInstance() {
        return HotSwap.getInstance().getSingleton(RestMgr.class);//自定义单例池获取单例
    }

    /**
     * 通过init 方法，传入 restTemplate 实例来设置 restTemplate
     *
     * @param restTemplateP
     * @return
     */
    public RestMgr init(RestTemplate restTemplateP) {
        this.restTemplate = restTemplateP;
        return this;
    }

    /**
     * 获取 RestTemplate 实例
     *
     * @return
     */
    public RestTemplate getTemplate() {
        return restTemplate;
    }
}
