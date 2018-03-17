package com.gecq.insurance.agent.service.utils;


import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存工具类，提供通用缓存方法
 */
public class CacheUtils {
    private CacheUtils(){}
    private static CacheUtils instance;
    public static CacheUtils instance(){
        return instance;
    }
    private  Map<Cache, MemcachedClient> cacheClientMap = new ConcurrentHashMap<Cache, MemcachedClient>();

    /**
     * 默认3天缓存
     */
    public static  final int DEFAULT_TIME_EXP = 3 * 24 * 3600;

    /**
     * 缓存一天
     */
    public static final int CACHE_ONE_DAY = 24*3600;

   public int getMinExp(int min){
       return min*60;
   }

    public enum Cache{
        defaultClient(),
        login(CACHE_ONE_DAY),
        admin(CACHE_ONE_DAY),
        product,
        promo,
        category,
        shoppingCart,
        customers(CACHE_ONE_DAY);
        Cache(int exp){
            this();
            this.exp=exp;
        }
        Cache(){
            this.key="key_"+this.name()+"_";
        }
        public String key(){
            return this.key;
        }
        private int exp=DEFAULT_TIME_EXP;
        private String key;
    }
    private Map<Cache,Map<String,String>> cachesMap;

    public Map<Cache, Map<String, String>> getCachesMap() {
        return cachesMap;
    }

    public void setCachesMap(Map<Cache, Map<String, String>> cachesMap) {
        this.cachesMap = cachesMap;
    }

    private void init() {
        System.out.println("init cache ....................");
        Set<Cache> cacheSet = cachesMap.keySet();
        for (Cache cache:cacheSet) {
                Map<String,String> info = cachesMap.get(cache);
                String host = info.get("host");
                String port = info.get("port");
                List<InetSocketAddress> socketAddresses = new ArrayList<InetSocketAddress>();
                socketAddresses.add(new InetSocketAddress(host, Integer.parseInt(port)));
                MemcachedClient memcachedClient = null;
                try {
                    memcachedClient = new MemcachedClient(new BinaryConnectionFactory(), socketAddresses);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cacheClientMap.put(cache, memcachedClient);
        }
        System.out.println("init cache ok!");
        flush(Cache.login);
        instance = this;
    }
    public void flushAll(){
        Cache[] caches = Cache.values();
        for(Cache c:caches){
            flush(c);
        }
    }



    /**
     * 存缓存，默认1小时过期
     * 如果原KEY存在就Replace掉，如果不存在就Add
     *
     * @param key 缓存KEY
     * @param obj 缓存Value
     */
    public void set(String key, Object obj) {
        set(key,obj,DEFAULT_TIME_EXP);
    }
    public void set(String key, Object obj,int exp) {
        MemcachedClient memcachedClient = getCache();
        if (memcachedClient != null) {
            memcachedClient.set(key, exp, obj);
        }
    }

    /**
     * 存缓存
     * 如果原KEY存在就Replace掉，如果不存在就Add
     *
     * @param key    缓存KEY
     * @param obj    缓存Value
     * @param cache 后缀
     */
    public void set(Cache cache, String key, Object obj) {
        if(obj==null){
            return;
        }
        MemcachedClient memcachedClient = getCache(cache);
        if (memcachedClient != null) {
            key = cache.key()+key;
            memcachedClient.set(key, cache.exp, obj);
        }
    }


    /**
     * 取缓存
     *
     * @param key 缓存KEY
     * @return 返回缓存Value
     */
    public Object get(String key) {
        MemcachedClient memcachedClient = getCache();
        if (memcachedClient != null) {
            return memcachedClient.get(key);
        }
        return null;
    }

    /**
     * 取缓存
     *
     * @param cache 缓存前缀
     * @param key    缓存KEY
     * @return 返回缓存Value
     */
    public Object get(Cache cache, String key) {
        MemcachedClient memcachedClient = getCache(cache);
        if (memcachedClient != null) {
            key = cache.key()+key;
            return memcachedClient.get(key);
        }
        return null;
    }

    public Object remove(Cache cache, String key) {
        MemcachedClient memcachedClient = getCache(cache);
        if (memcachedClient != null) {
            key = cache.key()+key;
            return memcachedClient.delete(key);
        }
        return null;
    }

    public void replace(Cache cache, String key, Object value, int time) {
        if(value==null){
            return;
        }
        MemcachedClient memcachedClient = getCache(cache);
        if (memcachedClient != null) {
            key = cache.key()+key;
            memcachedClient.replace(key, time, value);
        }
    }


    /**
     * 刷新缓存数据(使全部缓存过期)
     */
    public void flush() {
        flush(Cache.defaultClient);
    }

    /**
     * 刷新缓存数据(使全部缓存过期)
     *
     * @param cache 缓存前缀
     */
    public void flush(Cache cache) {
        MemcachedClient memcachedClient = getCache(cache);
        if (memcachedClient != null) {
            memcachedClient.flush();
        }
    }

    private MemcachedClient getCache() {
        return getCache(Cache.defaultClient);
    }

    private MemcachedClient getCache(Cache cache) {
        MemcachedClient client = cacheClientMap.get(cache);
        if (client == null) {
            return cacheClientMap.get(Cache.defaultClient);
        }
        return client;
    }

    public void close() {
        for (MemcachedClient client : cacheClientMap.values()) {
            if (client != null) {
                client.shutdown();
            }
        }
    }

}
