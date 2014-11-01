package jp.thotta.elrec.indexer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashMap;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;

public class CsvFileIndexerBatchTest extends TestCase {

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

  public void testRunIndex() {
    File file = new File("test/user_item.csv");
    CsvFileIndexerBatch indexBatch = new CsvFileIndexerBatch(file);
    try {
      indexBatch.doIndex();
    } catch(IOException e) {
      System.out.println(e.getMessage());
    }
    Jedis jedis = new Jedis("localhost");
    String items_csv;
    items_csv = jedis.get("user:3");
    assertTrue(items_csv.indexOf("3") != -1);
    assertTrue(items_csv.indexOf("2") == -1);
    System.out.println(items_csv);
    items_csv = jedis.get("user:190");
    assertTrue(items_csv.indexOf("420") != -1);
    assertTrue(items_csv.indexOf("432") == -1);
    System.out.println(items_csv);
    jedis.quit();
  }
}
