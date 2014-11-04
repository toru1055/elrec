package jp.thotta.elrec.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.Runnable;
import java.lang.InterruptedException;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;

import redis.clients.jedis.Jedis;

import jp.thotta.elrec.indexer.CsvFileIndexerBatch;
import jp.thotta.elrec.indexer.IndexCommandManager;
import jp.thotta.elrec.searcher.SearchCommandManager;

public class ServerRunnableTest extends TestCase {
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

  public void testSearchServerThread() throws IOException, UnknownHostException, InterruptedException {
    CommandManager cManager = new SearchCommandManager();
    ServerSetRunnable serverSetRunnable = new ServerSetRunnable(SERVER_PORT, cManager);
    Thread serverSetThread = new Thread(serverSetRunnable);
    serverSetThread.start();
    Socket cSock = new Socket("localhost", SERVER_PORT);
    BufferedReader in = new BufferedReader(new InputStreamReader(cSock.getInputStream()));
    PrintWriter out = new PrintWriter(cSock.getOutputStream(), true);
    out.println("Hello world.");
    String line = in.readLine();
    assertEquals(line, "null");
    String jsonCommand = "{'inputType' : 'item_id_list', 'itemIdList' : [1,4,6,400], 'howMany' : 10, 'includeKnownItems' : false}";
    out.println(jsonCommand);
    line = in.readLine();
    assertTrue(line.indexOf("\"itemId\":3") != -1);
    jsonCommand = "{'inputType' : 'item_id_list', 'itemIdList' : [100,200,300], 'howMany' : 10, 'includeKnownItems' : false}";
    out.println(jsonCommand);
    line = in.readLine();
    assertTrue(line.indexOf("{\"itemId\":299,\"score\":0.5}") > -1);
    jsonCommand = "{'inputType' : 'user_id', 'userId' : 13, 'howMany' : 10, 'includeKnownItems' : false}";
    out.println(jsonCommand);
    line = in.readLine();
    assertTrue(line.indexOf("{\"itemId\":67,\"score\":7.941062506660184}") > -1);
    out.println("");
    serverSetThread.join();
  }

  public void testIndexServerThread() throws IOException, UnknownHostException, InterruptedException {
    CommandManager cManager = new IndexCommandManager();
    ServerSetRunnable serverSetRunnable = new ServerSetRunnable(SERVER_PORT, cManager);
    Thread indexServerSetThread = new Thread(serverSetRunnable);
    indexServerSetThread.start();
    Socket cSock = new Socket("localhost", SERVER_PORT);
    BufferedReader in = new BufferedReader(new InputStreamReader(cSock.getInputStream()));
    PrintWriter out = new PrintWriter(cSock.getOutputStream(), true);
    out.println("Hello world.");
    String line = in.readLine();
    assertEquals(line, "null");
    String jsonCommand = "{'userId' : 1000000, 'itemId' : 2000000}";
    out.println(jsonCommand);
    line = in.readLine();
    assertEquals(line, "OK");
    out.println("{'userId' : 1000000, 'itemId' : 3000000}");
    out.println("{'userId' : 1000000, 'itemId' : 1000000}");
    out.println("{'userId' : 1000000, 'itemId' : 2000000}");
    out.println("");
    indexServerSetThread.join();
    Jedis jedis = new Jedis("localhost");
    assertTrue(jedis.get("user:1000000").indexOf("2000000") > -1);
    assertTrue(jedis.get("user:1000000").indexOf("3000000") > -1);
    assertTrue(jedis.get("user:1000000").indexOf("5000000") == -1);
    jedis.quit();
  }

  class ServerSetRunnable implements Runnable {
    private ServerSocket fServerSocket;
    private CommandManager cManager;

    public ServerSetRunnable(int port, CommandManager cm) throws IOException {
      this.fServerSocket = new ServerSocket(port);
      this.cManager = cm;
    }

    public void run() {
      try {
        Socket sSock = fServerSocket.accept();
        ServerRunnable serverRunnable = new ServerRunnable(sSock, 0, cManager);
        Thread serverThread = new Thread(serverRunnable);
        serverThread.start();
        serverThread.join();
        sSock.close();
      } catch(IOException e) {
        e.printStackTrace();
      } catch(InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
