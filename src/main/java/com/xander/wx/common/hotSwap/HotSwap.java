package com.xander.wx.common.hotSwap;


import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 自定义单例池
 * 注意：这里的单例没有限制 bean 的实例是绝对的单例，所以 bean 的 构造方法修饰符可以是 public
 *
 * @author Xander
 * datetime: 2019/8/19 11:05
 */
public class HotSwap {

    private static HotSwap instance = new HotSwap();

    private ConcurrentHashMap<Class<?>, Object> objPool = new ConcurrentHashMap<Class<?>, Object>();

    public static HotSwap getInstance() {
        return instance;
    }

    /**
     * 获取单例
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getSingleton(Class<T> clazz) {
        Object obj = objPool.get(clazz);
        if (obj == null) {
            try {
                Constructor<T> cons = clazz.getDeclaredConstructor();
                cons.setAccessible(true);
                obj = cons.newInstance();
                objPool.putIfAbsent(clazz, obj);
            } catch (Exception e) {
                throw (new RuntimeException("hotswap[getSingleton] error", e));
            }
        }
        return (T) objPool.get(clazz);
    }

}
