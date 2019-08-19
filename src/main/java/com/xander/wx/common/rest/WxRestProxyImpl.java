package com.xander.wx.common.rest;

import com.xander.wx.common.exception.WxRestException;
import com.xander.wx.common.hotSwap.HotSwap;
import com.xander.wx.common.json.JsonUtil;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class WxRestProxyImpl implements IntfWxRestProxy {

    public static final String EXCEPTION_TIPS = "网络请求出现异常";

    public static WxRestProxyImpl getInstance() {
        return HotSwap.getInstance().getSingleton(WxRestProxyImpl.class);//自定义单例池获取单例
    }

    /**
     * 获取 RestTemplate
     *
     * @return
     */
    private RestTemplate getTemplate() {
        // 通过 RestMgr 获取 RestTemplate
        return RestMgr.getInstance().getTemplate();
    }

    /**
     * 构建 公共的 请求头
     *
     * @return
     */
    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(acceptableMediaTypes);
        return headers;
    }

    private <T> ResponseEntity<T> send(String url, HttpMethod method, HttpEntity<String> entity, Class<T> clazz) {
        ResponseEntity<T> respEntity = null;
        try {
            respEntity = getTemplate().exchange(url, method, entity, clazz);
        } catch (RuntimeException e) {
            throw (WxRestException.newInstance(EXCEPTION_TIPS, e));
        }
        return respEntity;
    }

    @Override
    public <T> T post(String url, Object postParam, Class<T> clazz) {
        HttpHeaders headers = this.buildHeaders();
        String jsonTarget = JsonUtil.getInstance().toJson(postParam);
        HttpEntity<String> entity = new HttpEntity<String>(jsonTarget, headers);
        T tReturn = null;
        ResponseEntity<T> resp = send(url, HttpMethod.POST, entity, clazz);
        if (resp.getStatusCode().is2xxSuccessful()) {// 请求成功
            tReturn = resp.getBody();
        }
        return tReturn;
    }

    @Override
    public <T> T get(String url, Class<T> clazz) {
        HttpHeaders headers = this.buildHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        T tReturn = null;
        ResponseEntity<T> resp = send(url, HttpMethod.GET, entity, clazz);
        if (resp.getStatusCode().is2xxSuccessful()) {// 请求成功
            tReturn = resp.getBody();
        }
        return tReturn;
    }
}
