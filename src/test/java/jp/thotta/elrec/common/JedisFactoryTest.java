package jp.thotta.elrec.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import redis.clients.jedis.Jedis;

public class JedisFactoryTest extends TestCase {
  protected void setUp() {
    Jedis jedis = new Jedis("localhost");
    jedis.flushDB();
    jedis.quit();
  }

  protected void tearDown() {
    Jedis jedis = new Jedis("localhost");
    jedis.flushDB();
    jedis.quit();
  }

  public void testGetInstance() {
    System.out.println("Testing: JedisFactory");
    Jedis jedis = JedisFactory.getInstance();
    jedis.set("foo", "bar");
    String value = jedis.get("foo");
    assertEquals(value, "bar");
  }
}
