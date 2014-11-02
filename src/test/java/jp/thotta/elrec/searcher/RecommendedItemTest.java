package jp.thotta.elrec.searcher;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RecommendedItemTest extends TestCase {
  protected void setUp() {
  }
  protected void tearDown() {
  }

  public void testGetItemIdAndScore() {
    RecommendedItem rItem = new RecommendedItem(1000,0.5);
    assertEquals(rItem.getItemId(), (long)1000);
    assertEquals(rItem.getScore(), (double)0.5, 0.000001);
  }
}
