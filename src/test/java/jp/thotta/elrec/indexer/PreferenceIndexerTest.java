package jp.thotta.elrec.indexer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import redis.clients.jedis.Jedis;

public class PreferenceIndexerTest extends TestCase {

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

  public void testAddAndDelete() {
    Jedis jedis = new Jedis("localhost");
    PreferenceIndexer p = new PreferenceIndexer();
    p.addPreference(1,2);
    p.addPreference(1,3);
    p.addPreference(1,1);
    p.addPreference(2,3);
    p.addPreference(2,4);
    p.addPreference(3,1);
    p.addPreference(3,3);
    String items_csv = jedis.get("user:1");
    assertEquals(items_csv, "1,2,3");
    p.delPreference(1,2);
    assertEquals(jedis.get("user:1"), "1,3");
    jedis.quit();
  }

}
