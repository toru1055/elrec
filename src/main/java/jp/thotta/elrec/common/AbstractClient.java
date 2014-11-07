package jp.thotta.elrec.common;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;

public abstract class AbstractClient {
  protected static final int SEARCH_PORT = 1055;
  protected static final int INDEX_PORT = 1056;
  private int fServerPort;
  private String fClientName;
  private String fServerHost = "localhost";

  public AbstractClient(int defaultPort, String clientName) {
    this.fServerPort = defaultPort;
    this.fClientName = clientName;
  }

  private void parseOptions(String[] args) {
    for (int i=0; i<args.length; i++) {
      if("-p".equals(args[i])) {
        fServerPort = Integer.parseInt(args[++i]);
      } else if("-h".equals(args[i])) {
        fServerHost = args[++i];
      }
    }
  }

  public void runClient(String[] args) {
    Socket cSock = null;
    parseOptions(args);
    try {
      cSock = new Socket(fServerHost, fServerPort);
      BufferedReader in = new BufferedReader(new InputStreamReader(cSock.getInputStream()));
      PrintWriter out = new PrintWriter(cSock.getOutputStream(), true);
      BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));
      String input;
      System.out.print("[" + fClientName + "] ");
      while((input = keyIn.readLine()) != null) {
        if(input.equals("quit")) {
          out.println("");
          break;
        }
        if(input.length() > 0) {
          out.println(input);
          String line = in.readLine();
          if(line != null) {
            System.out.println(line);
          } else {
            break;
          }
        }
        System.out.print("[" + fClientName + "] ");
      }
    } catch(IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if(cSock != null) {
          cSock.close();
        }
      } catch(IOException e) {
      }
    }
  }
}
