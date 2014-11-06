package jp.thotta.elrec.searcher;

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

import jp.thotta.elrec.indexer.CsvFileIndexerBatch;

public class SearchServerTest extends TestCase {
  private static final int SERVER_PORT = 1055;

  protected void setUp() throws IOException {
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

  protected void tearDown() throws InterruptedException {
    Jedis jedis = new Jedis("localhost");
    jedis.flushDB();
    jedis.quit();
  }

  public void testMainNoMainRunning() {
    String[] a = new String[2];
    a[0] = "-t";
    a[1] = "0";
    SearchServer.main(a);
  }

  public void testDoSearchServerThread() throws UnknownHostException, IOException, InterruptedException {
    SearchServerRunnable ssr = new SearchServerRunnable();
    Thread searchServerThread = new Thread(ssr);
    searchServerThread.start();
    Thread.sleep(1000);
    Socket cSock = new Socket("localhost", SERVER_PORT);
    BufferedReader in = new BufferedReader(new InputStreamReader(cSock.getInputStream()));
    PrintWriter out = new PrintWriter(cSock.getOutputStream(), true);
    String jsonCommand, line;
    jsonCommand = "{'inputType' : 'item_id_list', 'itemIdList' : [1,4,6,400], 'howMany' : 10, 'includeKnownItems' : false}";
    out.println(jsonCommand);
    line = in.readLine();
    assertTrue(line.indexOf("\"itemId\":3") != -1);
    jsonCommand = "{'inputType' : 'user_id', 'userId' : 13, 'howMany' : 10, 'includeKnownItems' : false}";
    out.println(jsonCommand);
    line = in.readLine();
    assertTrue(line.indexOf("{\"itemId\":67,\"score\":") > -1);
    out.println("");
    cSock.close();
    searchServerThread.join();
  }

  class SearchServerRunnable implements Runnable {
    public void run() {
      String[] a = new String[2];
      a[0] = "-t";
      a[1] = "1";
      SearchServer.main(a);
    }
  }
}
