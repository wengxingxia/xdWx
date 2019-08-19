package com.xander.wx.common.json;


import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Description: Json 工具类
 *
 * @author Xander
 * datetime: 2019/8/19 11:36
 */
public class JsonUtil {

    private static JsonUtil instance = new JsonUtil();

    public static JsonUtil getInstance() {
        return instance;
    }

    public String toJson(Object target) {
        String result = JSON.toJSONString(target);
        return result;
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        T target = JSON.parseObject(json, clazz);
        return target;
    }

    public <T> List<T> parseList(String json, Class<T> clazz) {
        List<T> result = JSON.parseArray(json, clazz);
        return result;
    }

}
