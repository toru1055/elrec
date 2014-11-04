package jp.thotta.elrec.searcher;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.IOException;

import redis.clients.jedis.Jedis;
import jp.thotta.elrec.indexer.CsvFileIndexerBatch;

public class SearchCommandManagerTest extends TestCase {

  protected void setUp() {
    Jedis jedis = new Jedis("localhost");
    jedis.flushDB();
    jedis.quit();
    File file = new File("test/user_item.csv");
    CsvFileIndexerBatch indexBatch = new CsvFileIndexerBatch(file);
    try {
      indexBatch.doIndex();
    } catch(IOException e) {
      System.out.println(e.getMessage());
    }
  }

  protected void tearDown() {
    Jedis jedis = new Jedis("localhost");
    jedis.flushDB();
    jedis.quit();
  }

  public void testExecute() {
    SearchCommandManager scm = new SearchCommandManager();
    String jsonCommand = "{'inputType' : 'item_id_list', 'itemIdList' : [1,4,6,400], 'howMany' : 10, 'includeKnownItems' : false}";
    String result = scm.execute(jsonCommand);
    assertTrue(result.indexOf("{\"itemId\":3,\"score\":0.7559289460184544}") > -1);
    assertTrue(result.indexOf("{\"itemId\":405") == -1);
  }
}
