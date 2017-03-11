package com.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisUtil {
    
    private static JedisPool jedisPool = null;
    private static RedisPoolConfig redisPoolConfig = RedisPoolConfig.getRedisPoolConfig();
    
    private static RedisUtil redisUtil = null;
    
    /**
     * 初始化Redis连接池
     */
    private RedisUtil(){
    	 try {
         	/*
         	 * 高版本jedis中使用了org.apache.commons.pool2.impl.GenericObjectPoolConfig
         	 * 把"maxActive" 改成了 "maxTotal" 和 "maxWait" 改成了 "maxWaitMillis" 
         	 */
         	JedisPoolConfig config = new JedisPoolConfig();
             config.setMaxTotal(redisPoolConfig.MAX_TOTAL);
             config.setMaxIdle(redisPoolConfig.MAX_IDLE);
             config.setMaxWaitMillis(redisPoolConfig.MAX_WAIT_MILLIS);
             config.setTestOnBorrow(redisPoolConfig.TEST_ON_BORROW);
             jedisPool = new JedisPool(config, redisPoolConfig.IP, 
             		redisPoolConfig.PORT,redisPoolConfig.TIMEOUT);
         } catch (Exception e) {
             e.printStackTrace();
         }
    }
    
    /**
     * 
     * 方法说明： 获取RedisUtil实例<br>
     * 创建时间：2016-11-18 下午4:27:45 <br>
     * @return 
     *
     */
    public static RedisUtil getRedisUtil(){
    	
    	if (redisUtil == null) {
			synchronized (RedisUtil.class) {
				if (redisUtil == null) {
					redisUtil = new RedisUtil();
				}
			}
		}
    	
		return redisUtil;
    }
    
    
    /**
     * 正常情况下从连接池中获取Jedis实例
     * @return
     */
    public synchronized Jedis getJedis() {
    	Jedis jedis = null;
        try {
            if (jedisPool != null) {
                jedis = jedisPool.getResource();
                return jedis;
            } else {
                return null;
            }
        } catch (JedisConnectionException e) {
			System.out.println("无法从jedis池中获取资源！(有可能是redis服务器的问题)");
			e.printStackTrace();
			returnBrokenResource(jedis);
		} 
        return null;
    }
    
    /**
	 * 异常情况下销毁jedis连接池
	 * @param jedis
	 * @param msgId
	 */
	public static void returnBrokenResource(Jedis jedis){
		if(jedis==null)
			return;
		jedisPool.returnBrokenResource(jedis);
	}
    
    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }
    
    /**
	 * 关闭jedis连接池
	 */
	public static void closeJedisPool(){
		try {
			if(jedisPool != null){
				jedisPool.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public static void main(String[] args) {
    	RedisUtil redisUtil = RedisUtil.getRedisUtil();
		Jedis jedis = redisUtil.getJedis(); //创建
		jedis.set("jiang", "20000");
		System.out.println(jedis.get("jiang"));
		jedis.append("jiang", "元");
		System.out.println(jedis.get("jiang"));
		jedis.hset("xin", "A", "1");
		jedis.hset("xin", "B", "2");
		jedis.hset("xin", "C", "3");
		System.out.println(jedis.hget("xin", "A"));
		returnResource(jedis);
   	}
    
}