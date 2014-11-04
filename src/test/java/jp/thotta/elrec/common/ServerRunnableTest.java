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

public class ServerRunnableTest extends TestCase {
  private static final int SERVER_PORT = 1055;
  protected void setUp() throws IOException {
    ServerSetRunnable serverSetRunnable = new ServerSetRunnable(SERVER_PORT);
    Thread serverSetThread = new Thread(serverSetRunnable);
    serverSetThread.start();
  }

  public void testSingleServerThread() throws IOException, UnknownHostException {
    Socket cSock = new Socket("localhost", SERVER_PORT);
    BufferedReader in = new BufferedReader(new InputStreamReader(cSock.getInputStream()));
    PrintWriter out = new PrintWriter(cSock.getOutputStream(), true);
    out.println("Hello world.");
    out.println("{'inputType' : 'itemIdList', 'itemIds' : [], ");
    out.println("");
  }

  class ServerSetRunnable implements Runnable {
    private ServerSocket fServerSocket;

    public ServerSetRunnable(int port) throws IOException {
      this.fServerSocket = new ServerSocket(port);
    }

    public void run() {
      try {
        Socket sSock = fServerSocket.accept();
        ServerRunnable serverRunnable = new ServerRunnable(sSock, 0);
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
