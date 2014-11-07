package jp.thotta.elrec.common;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.List;
import java.util.ArrayList;

public abstract class AbstractServer {
  protected static final int SEARCH_PORT = 1055;
  protected static final int INDEX_PORT = 1056;
  private int fServerPort;
  private boolean fIsTest = false;
  private int fNumTestThreads = 0;
  private String fServerName;

  public AbstractServer(int defaultPort, String serverName) {
    this.fServerPort = defaultPort;
    this.fServerName = serverName;
  }

  private void parseOptions(String[] args) {
    for (int i=0; i<args.length; i++) {
      if("-t".equals(args[i])) {
        fIsTest = true;
        fNumTestThreads = Integer.parseInt(args[++i]);
      } else if("-p".equals(args[i])) {
        fServerPort = Integer.parseInt(args[++i]);
      }
    }
  }

  public void runServer(String[] args) {
    parseOptions(args);
    int threadCounter = 0;
    ServerSocket serverSocket = null;
    ServerRunnable serverRunnable = null;
    ExecutorService ex = createExecutorService();
    List<Future<Long>> list = new ArrayList<Future<Long>>();
    System.out.println(fServerName + " has started with port: " + fServerPort);
    try {
      serverSocket = new ServerSocket(fServerPort);
      CommandManager cManager = createCommandManager();
      while(threadCounter++ < fNumTestThreads || fIsTest == false) {
        Socket sSock = serverSocket.accept();
        serverRunnable = new ServerRunnable(sSock, threadCounter, cManager);
        Future<Long> future = (Future<Long>)ex.submit(serverRunnable);
        list.add(future);
      }
      System.out.println(fServerName + " has been finished port: " + fServerPort);
      for(Future<Long> future : list) {
        try {
          future.get();
        } catch(ExecutionException e) {
          e.printStackTrace();
        } catch(InterruptedException e) {
          e.printStackTrace();
        }
      }
    } catch(IOException e) {
      e.printStackTrace();
    } finally {
      ex.shutdownNow();
      System.out.println(fServerName + " has been finalized port: " + fServerPort);
      try {
        if (serverSocket != null) {
          serverSocket.close();
        }
      } catch(IOException e) { }
    }
  }

  protected abstract CommandManager createCommandManager();
  protected abstract ExecutorService createExecutorService();
}
