package com.test;

import java.util.ResourceBundle;

public class RedisPoolConfig {

	private static RedisPoolConfig redisPoolConfig = null;
	
	public  String  IP = "";  				//Redis服务器IP
	public  Integer PORT = null;			//Redis端口号
	public  Integer MAX_TOTAL = null;		//可用连接实例的最大数目，默认值为8；
	public  Integer MAX_IDLE = null;		//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例.
	public  Integer MAX_WAIT_MILLIS = null;	//等待可用连接的最大时间，单位毫秒.
	public  Boolean TEST_ON_BORROW = true;	//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	public  Integer TIMEOUT = null;			//连接池闲置多少秒后，断开连接
	public 	String 	hostInfo = "";			//用于可配置的Redis服务器上，主机基本信息hostInfo
	public 	String	procRt = "";			//用于可配置的Redis服务器上，进程实时键值procRt
		
	private RedisPoolConfig(){
		try {
			//从配置文件中获得数据库的配置信息(配置文件必须放在src目录下)
			ResourceBundle resourceBundle = ResourceBundle.getBundle("redisConfig");
			IP = resourceBundle.getString("redisIp");
			PORT = Integer.parseInt(resourceBundle.getString("redisPort"));
			MAX_TOTAL = Integer.parseInt(resourceBundle.getString("redisMaxTotal"));
			MAX_IDLE = Integer.parseInt(resourceBundle.getString("redisMaxIdle"));
			MAX_WAIT_MILLIS = Integer.parseInt(resourceBundle.getString("redisMaxWaitMillis"));
			TEST_ON_BORROW = resourceBundle.getString("redisTestOnBorrow") != null;
			TIMEOUT = Integer.parseInt(resourceBundle.getString("redisTimeout"));
			hostInfo = resourceBundle.getString("hostInfo");
			procRt = resourceBundle.getString("procRt");
		} catch (Exception e) {
			System.out.println(this.getClass().getName()+" 获得数据库的配置信息异常！");
			e.printStackTrace();
		} 
	}
	
	public static RedisPoolConfig getRedisPoolConfig(){
		if(redisPoolConfig == null){
			synchronized (RedisPoolConfig.class) {
				if (redisPoolConfig == null) {
					redisPoolConfig = new RedisPoolConfig();
				}
			}
			
		}
		return redisPoolConfig;
	} 
	
	public static void main(String[] args) {
		Thread[] threads = new Thread[100];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(){
				@Override
				public void run(){
					System.out.println(Thread.currentThread().getId()+" "+RedisPoolConfig.getRedisPoolConfig().hashCode());;
				}
			};
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		
	}
	
}
