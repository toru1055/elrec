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

  public static void main(String[] args) {
    int serverThreadMax = 0;
    if(args.length > 0) {
      // argsでThread数を指定すべきかどうか
      // でもこうしないとテストできない
      // --testオプションでも作るかな
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
