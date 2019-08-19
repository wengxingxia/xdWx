package com.xander.wx.common.cache;

import com.xander.wx.common.hotSwap.HotSwap;
import com.xander.wx.common.json.JsonUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 微信缓存管理类，对 缓存操作接口的 具体实现，生产环境可以通过 redis 来具体实现
 * 这里仅是通过 map 缓存在当前内存中，生产环境应缓存到 缓存中间件中，如： redis
 *
 * @author Xander
 * datetime: 2019/8/19 14:37
 */
public class CacheProxyImpl implements IntfCacheProxy {

    /**
     * 缓存的 value 都要先转为 json 字符串，在缓存进入map 中
     */
    private Map<String, Map<String, String>> cacheMap = new HashMap<String, Map<String, String>>();

    public static CacheProxyImpl getInstance() {
        return HotSwap.getInstance().getSingleton(CacheProxyImpl.class);//自定义单例池获取单例
    }

    private Map<String, String> getMap(String cacheName) {
        Map<String, String> map = cacheMap.get(cacheName);
        if (map == null) {
            map = new HashMap<String, String>();
            cacheMap.put(cacheName, map);
        }
        return map;
    }

    @Override
    public <T> void put(String cacheName, String key, T value) {
        if (value == null)
            return;
        Map<String, String> map = getMap(cacheName);
        // 将 T 先转为 json，再放入缓存中
        String jsonValue = JsonUtil.getInstance().toJson(value);
        map.put(key, jsonValue);
    }

    @Override
    public <T> T get(String cacheName, String key, Class<T> tClass) {
        Map<String, String> map = getMap(cacheName);
        String jsonValue = map.get(key);// 这里获取的是 缓存value对应的 json 字符串
        T target = null;
        if (!StringUtils.isEmpty(jsonValue)) {
            // 将 json 转为 T 实例
            target = JsonUtil.getInstance().fromJson(jsonValue, tClass);
        }
        return target;
    }

    @Override
    public void remove(String cacheName, String key) {
        Map<String, String> map = getMap(cacheName);
        if (CollectionUtils.isEmpty(map))
            return;
        if (map.get(key) != null)
            map.remove(key);
    }

}
