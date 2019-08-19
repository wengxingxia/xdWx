package com.xander.wx.common.cache;

/**
 * Description: 缓存代理接口，定义对缓存数据的操作
 * 后期需要另外的缓存操作，可通过该接口进行扩展
 *
 * @author Xander
 * datetime: 2019/8/19 14:43
 */
public interface IntfCacheProxy {

    /**
     * 放入缓存
     *
     * @param cacheName 域的名称，比如：WxAccessToken
     * @param key       cacheName的域下，缓存记录对应的 key
     * @param value     缓存的value
     */
    <T> void put(String cacheName, String key, T value);

    /**
     * 获取缓存
     *
     * @param cacheName 域的名称，比如：WxAccessToken
     * @param key       cacheName的域下，缓存记录对应的 key
     * @return 缓存的value
     */
    <T> T get(String cacheName, String key, Class<T> tClass);

    /**
     * 删除缓存
     *
     * @param cacheName cacheName 域的名称，比如：WxAccessToken
     * @param key       cacheName的域下，缓存记录对应的 key
     */
    void remove(String cacheName, String key);
}
