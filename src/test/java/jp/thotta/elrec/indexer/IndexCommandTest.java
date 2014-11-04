package jp.thotta.elrec.indexer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

public class IndexCommandTest extends TestCase {

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

  public void testExecCommand() {
    Gson gson = new Gson();
    PreferenceIndexer p = new PreferenceIndexer();
    String jsonCommand = null;
    IndexCommand ic = null;
    jsonCommand = "{'userId' : 1, 'itemId' : 2}";
    ic = gson.fromJson(jsonCommand, IndexCommand.class);
    assertTrue(ic.execCommand(p));
    jsonCommand = "{'use' : 1, 'itemId' : 2}";
    ic = gson.fromJson(jsonCommand, IndexCommand.class);
    assertFalse(ic.execCommand(p));
    jsonCommand = "{'userId' : 1, 'itemId' : 3}";
    ic = gson.fromJson(jsonCommand, IndexCommand.class);
    assertTrue(ic.execCommand(p));
    jsonCommand = "{'userId' : 1, 'itemId' : 1}";
    ic = gson.fromJson(jsonCommand, IndexCommand.class);
    assertTrue(ic.execCommand(p));
    jsonCommand = "{'userId' : 1, 'itemId' : 2}";
    ic = gson.fromJson(jsonCommand, IndexCommand.class);
    assertTrue(ic.execCommand(p));
    jsonCommand = "{'userId' : 1, 'itemId' : 2}";
    ic = gson.fromJson(jsonCommand, IndexCommand.class);
    assertTrue(ic.execCommand(p));

    Jedis jedis = new Jedis("localhost");
    String items_csv = jedis.get("user:1");
    assertEquals(items_csv, "1,2,3");
    jedis.quit();
  }
}
