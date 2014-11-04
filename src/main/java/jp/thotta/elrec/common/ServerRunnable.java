package jp.thotta.elrec.common;

import java.lang.Runnable;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;

public class ServerRunnable implements Runnable {
  private Socket fSocket;
  private int fNumber;

  public ServerRunnable(Socket socket, int number) {
    this.fSocket = socket;
    this.fNumber = number;
  }

  public void run() {
    try {
      showMessage("Server thread " + fNumber + " was started.");
      InputStreamReader isr = new InputStreamReader(fSocket.getInputStream());
      BufferedReader in = new BufferedReader(isr);
      PrintWriter out = new PrintWriter(fSocket.getOutputStream(), true);
      String line;
      while((line = in.readLine()) != null) {
        if(line.length() == 0) {
          break;
        }
        showMessage("Command line: " + line);
      }
    } catch(IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if(fSocket != null) {
          fSocket.close();
        }
      } catch(IOException e) {}
    }
  }

  private void showMessage(String message) {
    String clientIp = fSocket.getInetAddress().toString();
    int clientPort = fSocket.getPort();
    System.out.println(
        "[Thread" + fNumber +
        ": clientIp=" + clientIp + 
        ", clientPort=" + clientPort + "] " +
        message);
  }
}
