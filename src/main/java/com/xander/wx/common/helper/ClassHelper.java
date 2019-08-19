package com.xander.wx.common.helper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Description: Class 相关辅助类
 *
 * @author Xander
 * datetime: 2019/8/19 15:24
 */
public class ClassHelper {

    /**
     * 获取泛型的 class
     *
     * @param t
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getEntityClass(T t) {

        Class<T> entityClass = null;
        Type type = t.getClass().getGenericSuperclass();
        while (!(type instanceof ParameterizedType)) {
            type = t.getClass().getSuperclass().getGenericSuperclass();
        }

        if (((ParameterizedType) type).getActualTypeArguments()[0] instanceof ParameterizedType) {
            entityClass = null;
        } else {
            entityClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        return entityClass;
    }

    /**
     * 获取泛型的 class
     *
     * @param classP
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getEntityClass(Class<?> classP) {

        Class<T> entityClass = null;
        Type type = classP.getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            type = classP.getSuperclass().getGenericSuperclass();
        }

        if (((ParameterizedType) type).getActualTypeArguments()[0] instanceof ParameterizedType) {
            entityClass = null;
        } else {
            entityClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        return entityClass;
    }

}
