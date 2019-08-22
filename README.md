# xdWx
java 对接微信公众号相关服务

### 1、新建一个 springboot 工程
![image.png](https://upload-images.jianshu.io/upload_images/3143211-bb7ffb21b8f67ee2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image.png](https://upload-images.jianshu.io/upload_images/3143211-27208180a3471d35.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image.png](https://upload-images.jianshu.io/upload_images/3143211-ef353603a66f990d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image.png](https://upload-images.jianshu.io/upload_images/3143211-0688845954957bb7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image.png](https://upload-images.jianshu.io/upload_images/3143211-bb8234d57244018d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 2、简单抽取 单例池 ，字符串格式化 ，class辅助类 ClassHelper，json 工具类
#### 2.1、单例池 **`HotSwap`**
注意：这里只是一个获取单例的辅助类，没有限制 bean 的实例是绝对的单例，所以 bean 的 构造方法修饰符可以是 public
```java
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


```

#### 2.2、字符串格式化 **`StringFormatUtil`**
```java
package com.xander.wx.common.utils;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * Description: 字符串格式化工具
 *
 * @author Xander
 * datetime: 2019/8/19 10:21
 */
public class StringFormatUtil {

    public static String format(String format, Object... args) {
        //example "this is an {} for use." example,{} 是占位符
        FormattingTuple ft = MessageFormatter.arrayFormat(format, args);
        return ft.getMessage();
    }


}

```
#### 2.3、Class 相关辅助类 **`ClassHelper`**
###### 用于获取 class 上 泛型 <T> 对应的 class
```java
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

```
#### 2.4、**`JsonUtil`**
**`pom.xml`**文件中加入 fastjson 依赖
```java
        <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.32</version>
        </dependency>

```

```java
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


```
### 3、自定义异常，记录微信相关网络请求异常
```java
package com.xander.wx.common.exception;


import com.xander.wx.common.utils.StringFormatUtil;

/**
 * Description: 自定义异常——主要用于 记录微信相关网络请求异常
 *
 * @author Xander
 * datetime: 2019/8/19 11:16
 */
public class WxRestException extends RuntimeException {

    private static final long serialVersionUID = 5857825376993092542L;

    final static private String msgFormat = "code:{}, tips:{}";

    private int statusCode;

    private String tips;

    private RuntimeException proxyErrorCause;

    private WxRestException(String msg, int statusCodeP, String tipsP, RuntimeException causeP) {

        super(msg, null);
        this.statusCode = statusCodeP;
        this.tips = tipsP;
        this.proxyErrorCause = causeP;
    }


    public static WxRestException newInstance(int statusCodeP, String tipsP) {
        String msg = StringFormatUtil.format(msgFormat, statusCodeP, tipsP);
        WxRestException instance = new WxRestException(msg, statusCodeP, tipsP, null);
        return instance;
    }

    public static WxRestException newInstance(String tipsP, RuntimeException causeP) {
        int statusCodeP = 500;
        String msg = StringFormatUtil.format(msgFormat, statusCodeP, tipsP);
        WxRestException instance = new WxRestException(msg, statusCodeP, tipsP, causeP);
        return instance;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getTips() {
        return tips;
    }

    public RuntimeException getCause() {
        return proxyErrorCause;
    }

}

```

### 4、简单封装 网络请求
###### 由于对接微信服务器，只需用到 get 和 post 请求，这里只针对 get 和 post 请求做简单的封装，并不是真正 restful API 的封装。
#### 4.1、Rest 管理类 **`RestMgr`**
##### 通过 init 方法设置 RestTemplate 实例，管理并为其他类提供 RestTemplate 实例
```java
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

```

#### 4.2、微信网络请求代理接口 **`IntfWxRestProxy`**
###### 这个接口主要是为了定义 网络请求相关的动作
```java
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
     * @param uri       请求 uri
     * @param postParam 表单参数
     * @param clazz     Class
     * @param <T>
     * @return
     */
    <T> T post(String uri, Object postParam, Class<T> clazz);

    /**
     * GET 请求
     *
     * @param uri   请求 uri
     * @param clazz Class
     * @param <T>
     * @return
     */
    <T> T get(String uri, Class<T> clazz);
}

```
#### 4.3、网络请求代理的实现类 ——**`WxRestProxyImpl`**
######  网络请求动作的具体实现——RestTemplate 发送网络请求的封装，还有统一封装网络请求异常
```java
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

```
#### 4.4、微信网络请求基类 dao —— **`WxRestDao`**
###### 这里主要是封装 url 的拼接 和 调用 IntfWxRestProxy 网络请求代理来发送网络请求。
###### 所有业务层的 xxxDao 都需要 extends WxRestDao 来集成网络请求相关方法。具体使用可参考后面章节 WxAccessToekn 的相关网络请求。
```java
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

```

### 5、封装缓存
###### **`注意：`**这里仅是通过 map 缓存在当前内存中，生产环境应缓存到 缓存中间件中，如： redis

#### 5.1、缓存代理接口 **`IntfCacheProxy`**
###### 缓存代理接口，定义对缓存数据的操作，后期如果需要添加新的缓存操作，可通过该接口进行扩展
```java
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

```
#### 5.2、缓存操作接口的具体实现 **`CacheProxyImpl`**
###### 这里仅是通过 map 缓存在当前内存中，生产环境可以通过 redis 来具体实现，将缓存数据放置到 redis 中
```java
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

```
#### 5.3、缓存操作基类，对不同对象的缓存操作进行隔离 **`CacheDao<T>`**
###### 如果仅仅是通过工具类来操作缓存，则对工具类的调用会渗透到各个模块的业务代码中，会加大后期的维护成本.
###### 这里通过 泛型<T> 来对不同的域的缓存操作进行隔离。所有业务层的缓存操作类 xxxCacheDao 都需要 extends CacheDao<T> 来集成缓存操作相关方法。具体使用可参考后面章节 WxAccessToken 的缓存操作。
```java
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

```
##### 代码：

[[Java微信服务] No.0 新建工程，封装工具类，网络请求和缓存](https://www.jianshu.com/p/45ca4156afb2)

