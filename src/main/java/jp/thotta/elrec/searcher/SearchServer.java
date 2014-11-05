package jp.thotta.elrec.searcher;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import jp.thotta.elrec.common.CommandManager;
import jp.thotta.elrec.common.ServerRunnable;

public class SearchServer {
  // ここはデフォルト値にして、Configファイルか
  // argsでポート番号指定できるようにしたい
  private static final int SEARCH_PORT = 1055;
  private static boolean isTest = false;
  private static int numTestThreads = 0;

  private static void parseOptions(String[] args) {
    for (int i=0; i<args.length; i++) {
      if("-t".equals(args[i])) {
        isTest = true;
        numTestThreads = Integer.parseInt(args[++i]);
      }
    }
  }

  public static void main(String[] args) {
    int threadCounter = 0;
    ServerSocket serverSocket = null;
    ServerRunnable serverRunnable = null;
    System.out.println("SearchServer has started with port: " + SEARCH_PORT);
    parseOptions(args);
    try {
      serverSocket = new ServerSocket(SEARCH_PORT);
      CommandManager cManager = new SearchCommandManager();
      while(threadCounter++ < numTestThreads || isTest == false) {
        Socket sSock = serverSocket.accept();
        serverRunnable = new ServerRunnable(sSock, threadCounter, cManager);
        Thread serverThread = new Thread(serverRunnable);
        serverThread.start();
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
