package jp.thotta.elrec.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Map;
import java.util.HashMap;

public class BasePreferencesTest extends TestCase {
  public BasePreferencesTest(String testName) {
    super(testName);
  }

  public void testGetPreferenceIdsCsv() {
    BasePreferences p = new BasePreferences(100, "1,2,3,4,5,10");
    p.addPreference(9);
    p.addPreference(10);
    p.addPreference(100);
    String res = p.getPreferenceIdsCsv();
    System.out.println(res);
    assertTrue(res.indexOf("9") != -1);
    assertTrue(res.indexOf("5") != -1);
    assertTrue(res.indexOf("7") == -1);
    Map<Long,Boolean> list = p.getPreferenceIds();
    assertTrue(list.get((long)10));
    assertTrue(list.get((long)1));
    assertTrue(list.get((long)3));
    assertEquals(list.get((long)8), null);
    assertEquals(p.getId(), (long)100);
  }

}
