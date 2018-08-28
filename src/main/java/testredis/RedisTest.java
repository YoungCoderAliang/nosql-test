package testredis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisTest {
	public static void main(String[] args) {
		final Jedis jedis = new Jedis("10.128.168.81", 6379);
		jedis.connect();
		final JedisPubSub sub = new JedisPubSub() {
			public void onSubscribe(String channel, int subscribedChannels) {
				System.out.println("onSubscribe");
			}

			public void onUnsubscribe(String channel, int subscribedChannels) {
				System.out.println("onUnsubscribe");
				jedis.set("testkkk", "testvvv");
			}

			public void onMessage(String channel, String message) {
				System.out.println("onMessage");
			}
		};
		Thread t = new Thread(new Runnable() {
			public void run() {
				jedis.subscribe(sub, "sc");
			}
		});
		t.start();
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Jedis("10.128.168.81", 6379).publish("sc", "hello");
		sub.unsubscribe("sc");
	}
}
