package com.test;

import redis.clients.jedis.Jedis;

public class TestRedis {
	public static void main(String[] args) {

		try {
			String a = "proc:mw0104313:ie.exe:2016-09-09 10:00:00";

			String[] b = a.split(":");
			System.out.println(b[0] + "=====" + b[1]);

			//获取jedis实例
			RedisUtil redisUtil = RedisUtil.getRedisUtil();
			Jedis jedis = redisUtil.getJedis();
			if (jedis != null) {
				//往redis中set数据
				jedis.set(
						"proc:mdszdp0002:init:2016-09-09 10-00-00",
						"{\"level\": \"warning\",\"time\":\"2016-09-09 10:00:00\",\"proc_name\": \"firefox.exe\",\"pid\": 1234,\"module\": \"123\",\"instance\": \"01\",\"cpu\": \"cpu使用率\",\"memory\":\"内存使用量\",\"handle\": 1234,\"thread\": 222}");
				System.out.println(jedis
						.get("proc:mdszdp0002:init:2016-09-09 10-00-00"));
			}
			//释放资源
			RedisUtil.returnResource(jedis);
			//关闭连接
			RedisUtil.closeJedisPool();
			
		} catch (Exception e) {
			System.out.print("出错了。");
			e.printStackTrace();
		}

	}
}
