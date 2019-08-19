package com.xander.wx.common.cache;

import com.xander.wx.common.helper.ClassHelper;

/**
 * Description: 缓存操作基类，对不同对象的缓存操作进行隔离
 * 通过缓存代理 IntfCacheProxy 来操作缓存数据
 *
 * @author Xander
 * datetime: 2019/8/19 15:05
 */
public class CacheDao<T> {

    private Class<T> tClass = null;

    public CacheDao() {
        this.tClass = ClassHelper.getEntityClass(this.getClass());// 获取泛型的 Class 类型
    }

    /**
     * 获取缓存操作代理
     *
     * @return
     */
    public IntfCacheProxy getProxy() {
        return CacheProxyImpl.getInstance();
    }

    /**
     * 放入缓存
     *
     * @param cacheName 域的名称，比如：WxAccessToken
     * @param key       cacheName的域下，缓存记录对应的 key
     * @param value     缓存的value
     */
    public void put(String cacheName, String key, T value) {
        this.getProxy().put(cacheName, key, value);
    }

    /**
     * 获取缓存
     *
     * @param cacheName 域的名称，比如：WxAccessToken
     * @param key       cacheName的域下，缓存记录对应的 key
     * @return 缓存的value
     */
    public <T> T get(String cacheName, String key) {
        Object object = this.getProxy().get(cacheName, key, this.tClass);
        if (object != null) {
            return (T) object;
        } else {
            return null;
        }
    }

    /**
     * 删除缓存
     *
     * @param cacheName cacheName 域的名称，比如：WxAccessToken
     * @param key       cacheName的域下，缓存记录对应的 key
     */
    public void remove(String cacheName, String key) {
        this.getProxy().remove(cacheName, key);
    }

}
