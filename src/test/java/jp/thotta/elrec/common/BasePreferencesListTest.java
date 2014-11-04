package jp.thotta.elrec.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Map;
import java.util.HashMap;
import redis.clients.jedis.Jedis;

public class BasePreferencesListTest extends TestCase {
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

  public void testItemUsers() {
    ItemUsersPreferencesList itemUsers = new ItemUsersPreferencesList();
    itemUsers.add(111, 1);
    itemUsers.add(111, 2);
    itemUsers.add(111, 4);
    itemUsers.add(111, 1);
    itemUsers.add(111, 6);
    Map<Long,Boolean> m = itemUsers.getPreferenceIds(111);
    assertTrue(m.get((long)1));
    assertTrue(m.get((long)2));
    assertTrue(m.get((long)4));
    assertTrue(m.get((long)6));
    assertEquals(m.get((long)7), null);
  }

  public void testUserItems() {
    UserItemsPreferencesList userItems = new UserItemsPreferencesList();
    userItems.add(111, 1);
    userItems.add(111, 2);
    userItems.add(111, 4);
    userItems.add(111, 1);
    userItems.add(111, 6);
    Map<Long,Boolean> m = userItems.getPreferenceIds(111);
    assertTrue(m.get((long)1));
    assertTrue(m.get((long)2));
    assertTrue(m.get((long)4));
    assertTrue(m.get((long)6));
    assertEquals(m.get((long)7), null);
  }

}
