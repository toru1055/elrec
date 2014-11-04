package jp.thotta.elrec.searcher;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import jp.thotta.elrec.common.CommandManager;
import jp.thotta.elrec.common.ServerRunnable;

public class SearchServer {
  private static final int SEARCH_PORT = 1055;

  public static void main(String[] args) {
    int serverThreadMax = 0;
    if(args.length > 0) {
      serverThreadMax = Integer.parseInt(args[0]);
    }
    int threadCounter = 0;
    ServerSocket serverSocket = null;
    ServerRunnable serverRunnable = null;
    System.out.println("SearchServer: " + SEARCH_PORT);
    try {
      serverSocket = new ServerSocket(SEARCH_PORT);
      CommandManager cManager = new SearchCommandManager();
      while(serverThreadMax > threadCounter) {
        Socket sSock = serverSocket.accept();
        serverRunnable = new ServerRunnable(sSock, threadCounter, cManager);
        Thread serverThread = new Thread(serverRunnable);
        serverThread.start();
        threadCounter++;
      }
    } catch(IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (serverSocket != null) {
          serverSocket.close();
        }
      } catch(IOException e) { }
    }
  }
}
