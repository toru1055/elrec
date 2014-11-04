package jp.thotta.elrec.searcher;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.lang.Math;

import redis.clients.jedis.Jedis;

import jp.thotta.elrec.indexer.CsvFileIndexerBatch;

public class ItemBasedRecommenderTest extends TestCase {

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

  public void testMostSimilarItems() {
    List<Long> sourceItems = new ArrayList<Long>();
    sourceItems.add((long)1);
    sourceItems.add((long)4);
    sourceItems.add((long)6);
    sourceItems.add((long)400);
    ItemBasedRecommender r = new ItemBasedRecommender();
    List<RecommendedItem> rItemList = r.mostSimilarItems(sourceItems, 10, false);
    HashMap<Long,Double> itemMap = new HashMap<Long,Double>();
    for(RecommendedItem rItem : rItemList) {
      itemMap.put(rItem.getItemId(), rItem.getScore());
      System.out.println(
          "Recommended item: " + 
          rItem.getItemId() +
          ", score: " +
          rItem.getScore());
    }
    assertEquals(itemMap.get((long)3), 2.0/Math.sqrt(7), 0.00001);
    assertEquals(itemMap.get((long)5), 2.0/Math.sqrt(7), 0.00001);
    assertEquals(itemMap.get((long)9), 2.0/Math.sqrt(7), 0.00001);
    assertEquals(itemMap.get((long)401), 1.0/Math.sqrt(3), 0.00001);
    assertEquals(itemMap.get((long)402), 1.0/Math.sqrt(3), 0.00001);
  }

  protected void tearDown() {
    Jedis jedis = new Jedis("localhost");
    jedis.flushDB();
    jedis.quit();
  }
}
