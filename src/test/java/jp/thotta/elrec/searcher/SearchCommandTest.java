package jp.thotta.elrec.searcher;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import jp.thotta.elrec.indexer.CsvFileIndexerBatch;

public class SearchCommandTest extends TestCase {

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

  public void testRecommendFromItemIdList() {
    Gson gson = new Gson();
    ItemBasedRecommender r = new ItemBasedRecommender();
    List<RecommendedItem> rItems = null;
    String jsonCommand = null;
    jsonCommand = "{'inputType' : 'item_id_list', 'itemIdList' : [1,4,6,400], 'howMany' : 10, 'includeKnownItems' : false}";
    SearchCommand sc = gson.fromJson(jsonCommand, SearchCommand.class);
    rItems = sc.execCommand(r);
    Map<Long,Double> itemMap = new HashMap<Long,Double>();
    for(RecommendedItem rItem : rItems) {
      itemMap.put(rItem.getItemId(), rItem.getScore());
    }
    assertEquals(itemMap.get((long)3), 2.0/Math.sqrt(7), 0.00001);
    assertEquals(itemMap.get((long)5), 2.0/Math.sqrt(7), 0.00001);
    assertEquals(itemMap.get((long)9), 2.0/Math.sqrt(7), 0.00001);
    assertEquals(itemMap.get((long)401), 1.0/Math.sqrt(3), 0.00001);
    jsonCommand = "{'inputType' : 'item_id', 'itemIdList' : [1,4,6,400], 'howMany' : 10, 'includeKnownItems' : false}";
    sc = gson.fromJson(jsonCommand, SearchCommand.class);
    rItems = sc.execCommand(r);
    assertNull(rItems);
  }

  protected void tearDown() {
    Jedis jedis = new Jedis("localhost");
    jedis.flushDB();
    jedis.quit();
  }
}
