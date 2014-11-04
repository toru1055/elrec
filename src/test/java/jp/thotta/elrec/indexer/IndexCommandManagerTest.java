package jp.thotta.elrec.indexer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import redis.clients.jedis.Jedis;

public class IndexCommandManagerTest extends TestCase {
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
 
  public void testExecute() {
    IndexCommandManager icm = new IndexCommandManager();
    assertEquals(icm.execute("{'userId' : 1, 'itemId' : 2}"), "OK");
    assertNull(icm.execute("{'use' : 1, 'itemId' : 2}"));
    assertNull(icm.execute("{'use' : 1, 'itemId' "));
    assertEquals(icm.execute("{'userId' : 1, 'itemId' : 3}"), "OK");
    assertEquals(icm.execute("{'userId' : 1, 'itemId' : 1}"), "OK");
    assertEquals(icm.execute("{'userId' : 1, 'itemId' : 2}"), "OK");
    assertEquals(icm.execute("{'userId' : 1, 'itemId' : 2}"), "OK");
    Jedis jedis = new Jedis("localhost");
    String items_csv = jedis.get("user:1");
    assertEquals(items_csv, "1,2,3");
    jedis.quit();
  }
}
