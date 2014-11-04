package jp.thotta.elrec.searcher;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.Runnable;

public class SearchServerTest extends TestCase {
  public void testMainNoMainRunning() {
    String[] a = new String[1];
    a[0] = "0";
    SearchServer.main(a);
  }

  public void testDoSearchServerThread() {
    // make SearchServerRunnable.class
    // make and start Thread instance
    // start client program
  }

  // Have to make runnable class to create the server thread
  // for SearchServer.
  class SearchServerRunnable implements Runnable {
    public void run() {
      String[] a = new String[1];
      a[0] = "1";
      SearchServer.main(a);
    }
  }
}
