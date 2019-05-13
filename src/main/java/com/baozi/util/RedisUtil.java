package com.baozi.util;

import com.baozi.util.spring.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Copyright:   西邻在线电子商务
 *
 * @author: zhangwenjun
 * @version: V1.0
 * @Date: 2019-05-13 14:19
 */
public class RedisUtil {

    final static StringRedisTemplate stringRedisTemplate = SpringContextUtil.getBean("stringRedisTemplate",StringRedisTemplate.class);
    final static RedisTemplate<String, Object> redisTemplate = SpringContextUtil.getBean("redisTemplate",RedisTemplate.class);

    /**
     * 删除缓存<br>
     * 根据key精确匹配删除
     * @param key
     */
    @SuppressWarnings("unchecked")
    public static void del(String... key){
        if(key!=null && key.length > 0){
            if(key.length == 1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    public static void setToday(String key, Object value) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(now.getTime());
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DATE,1);//前一天
        long times = cal.getTimeInMillis() -  now.getTimeInMillis();
        setex(key, value, (int)(times/1000));
    }

    /**
     * 批量删除<br>
     * （该操作会执行模糊查询，请尽量不要使用，以免影响性能或误删）
     * @param pattern
     */
    public static void batchDel(String... pattern){
        for (String kp : pattern) {
            redisTemplate.delete(redisTemplate.keys(kp + "*"));
        }
    }

    /**
     * 取得缓存（int型）
     * @param key
     * @return
     */
    public static Integer getInt(String key){
        String value = stringRedisTemplate.boundValueOps(key).get();
        if(StringUtils.isNotBlank(value)){
            return Integer.valueOf(value);
        }
        return null;
    }

    /**
     * 取得缓存（字符串类型）
     * @param key
     * @return
     */
    public static String getStr(String key){
        return stringRedisTemplate.boundValueOps(key).get();
    }

    /**
     * 取得缓存（字符串类型）
     * @param key
     * @return
     */
    public static String getStr(String key, boolean retain){
        String value = stringRedisTemplate.boundValueOps(key).get();
        if(!retain){
            redisTemplate.delete(key);
        }
        return value;
    }

    /**
     * 获取缓存<br>
     * 注：基本数据类型(Character除外)，请直接使用get(String key, Class<T> clazz)取值
     * @param key
     * @return
     */
    public static Object getObj(String key){
        return redisTemplate.boundValueOps(key).get();
    }
    public static <T>T getObj(String key, Class<T> clazz){
        return (T)redisTemplate.boundValueOps(key).get();
    }

    /**
     * 获取缓存<br>
     * 注：java 8种基本类型的数据请直接使用get(String key, Class<T> clazz)取值
     * @param key
     * @param retain    是否保留
     * @return
     */
    public static Object getObj(String key, boolean retain){
        Object obj = redisTemplate.boundValueOps(key).get();
        if(!retain){
            redisTemplate.delete(key);
        }
        return obj;
    }

    /**
     * 获取缓存<br>
     * 注：该方法暂不支持Character数据类型
     * @param key   key
     * @param clazz 类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key, Class<T> clazz) {
        return (T)redisTemplate.boundValueOps(key).get();
    }

    /**
     * 将value对象写入缓存
     * @param key
     * @param value
     * @param time 失效时间(秒)
     */
    public static void setex(String key,Object value,int time){
        if(value.getClass().equals(String.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Integer.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Double.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Float.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Short.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Long.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Boolean.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else{
            redisTemplate.opsForValue().set(key, value);
        }
        if(time > 0){
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 递减操作
     * @param key
     * @param by
     * @return
     */
    public static double decr(String key, double by){
        return redisTemplate.opsForValue().increment(key, -by);
    }

    /**
     * 递增操作
     * @param key
     * @param by
     * @return
     */
    public static double incr(String key, double by){
        return redisTemplate.opsForValue().increment(key, by);
    }

    /**
     * 获取double类型值
     * @param key
     * @return
     */
    public static double getDouble(String key) {
        String value = stringRedisTemplate.boundValueOps(key).get();
        if(StringUtils.isNotBlank(value)){
            return Double.valueOf(value);
        }
        return 0d;
    }

    /**
     * 设置double类型值
     * @param key
     * @param value
     * @param time 失效时间(秒)
     */
    public static void setDouble(String key, double value, int time) {
        stringRedisTemplate.opsForValue().set(key, String.valueOf(value));
        if(time > 0){
            stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 设置 int 类型值
     * @param key
     * @param value
     * @param time 失效时间(秒)
     */
    public static void setInt(String key, int value, int time) {
        stringRedisTemplate.opsForValue().set(key, String.valueOf(value));
        if(time > 0){
            stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 将map写入缓存
     * @param key
     * @param map
     */
    @SuppressWarnings("unchecked")
    public static <T> void setMap(String key, Map map){
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 向key对应的map中添加缓存对象
     * @param key
     * @param map
     */
    public static <T> void addMap(String key, Map<String, T> map){
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 向key对应的map中添加缓存对象
     * @param key   cache对象key
     * @param field map对应的key
     * @param value     值
     */
    public static void addMap(String key, String field, String value){
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 向key对应的map中添加缓存对象
     * @param key   cache对象key
     * @param field map对应的key
     * @param obj   对象
     */
    public static <T> void addMap(String key, String field, T obj){
        redisTemplate.opsForHash().put(key, field, obj);
    }

    /**
     * 获取map缓存
     * @param key
     * @param clazz
     * @return
     */
    public static <T> Map<String, T> mget(String key, Class<T> clazz){
        BoundHashOperations<String, String, T> boundHashOperations = redisTemplate.boundHashOps(key);
        return boundHashOperations.entries();
    }

    /**
     * 获取map缓存中的某个对象
     * @param key
     * @param field
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getMapField(String key, String field, Class<T> clazz){
        return (T)redisTemplate.boundHashOps(key).get(field);
    }

    /**
     * 删除map中的某个对象
     * @author lh
     * @date 2016年8月10日
     * @param key   map对应的key
     * @param field map中该对象的key
     */
    public void delMapField(String key, String... field){
        BoundHashOperations<String, String, ?> boundHashOperations = redisTemplate.boundHashOps(key);
        boundHashOperations.delete(field);
    }

    /**
     * 指定缓存的失效时间
     *
     * @author FangJun
     * @date 2016年8月14日
     * @param key 缓存KEY
     * @param time 失效时间(秒)
     */
    public static void expire(String key, int time) {
        if(time > 0){
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 添加set
     * @param key
     * @param value
     */
    public static void sadd(String key, String... value) {
        redisTemplate.boundSetOps(key).add(value);
    }

    /**
     * 删除set集合中的对象
     * @param key
     * @param value
     */
    public static void srem(String key, String... value) {
        redisTemplate.boundSetOps(key).remove(value);
    }

    /**
     * 判断 set 集合里是否包含当前这个 key
     * @param setKey
     * @param key
     */
    public static Boolean sexist(String setKey, String  key) {
        return redisTemplate.boundSetOps(setKey).isMember(key);
    }

    /**
     * set重命名
     * @param oldkey
     * @param newkey
     */
    public static void srename(String oldkey, String newkey){
        redisTemplate.boundSetOps(oldkey).rename(newkey);
    }

    /**
     * 模糊查询keys
     * @param pattern
     * @return
     */
    public static Set<String> keys(String pattern){
        return redisTemplate.keys(pattern);
    }
}
