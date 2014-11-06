package jp.thotta.elrec.indexer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.Runnable;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.lang.InterruptedException;
import java.io.File;

import redis.clients.jedis.Jedis;

public class IndexServerTest extends TestCase {
  private static final int SERVER_PORT = 1056;

  protected void setUp() throws IOException {
    Jedis jedis = new Jedis("localhost");
    jedis.flushDB();
    jedis.quit();
  }

  protected void tearDown() throws InterruptedException {
    Jedis jedis = new Jedis("localhost");
    jedis.flushDB();
    jedis.quit();
  }

  public void testMainNoMainRunning() {
    String[] a = new String[2];
    a[0] = "-t";
    a[1] = "0";
    IndexServer.main(a);
  }

  public void testDoIndexServerThread() throws UnknownHostException, IOException, InterruptedException {
    IndexServerRunnable isr = new IndexServerRunnable();
    Thread indexServerThread = new Thread(isr);
    indexServerThread.start();
    Thread.sleep(1000);
    Socket cSock = new Socket("localhost", SERVER_PORT);
    BufferedReader in = new BufferedReader(new InputStreamReader(cSock.getInputStream()));
    PrintWriter out = new PrintWriter(cSock.getOutputStream(), true);
    String line;
    out.println("{'userId' : 1000000, 'itemId' : 2000000}");
    line = in.readLine();
    assertEquals(line, "OK");
    out.println("{'userId' : 1000000, 'itemId' : 3000000}");
    out.println("{'userId' : 1000000, 'itemId' : 1000000}");
    out.println("{'userId' : 1000000, 'itemId' : 2000000}");
    out.println("");
    cSock.close();
    indexServerThread.join();
    Jedis jedis = new Jedis("localhost");
    System.out.println("jedis.get(user:1000000): " + jedis.get("user:1000000"));
    assertTrue(jedis.get("user:1000000").indexOf("2000000") > -1);
    System.out.println("jedis.get(user:1000000): " + jedis.get("user:1000000"));
    assertTrue(jedis.get("user:1000000").indexOf("3000000") > -1);
    System.out.println("jedis.get(user:1000000): " + jedis.get("user:1000000"));
    assertTrue(jedis.get("user:1000000").indexOf("5000000") == -1);
    jedis.quit();
  }

  class IndexServerRunnable implements Runnable {
    public void run() {
      String[] a = new String[2];
      a[0] = "-t";
      a[1] = "1";
      IndexServer.main(a);
    }
  }
}
