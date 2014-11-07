package jp.thotta.elrec.searcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.thotta.elrec.common.CommandManager;
import jp.thotta.elrec.common.AbstractServer;

public class SearchServer extends AbstractServer {
  public SearchServer(int defaultPort, String serverName) {
    super(defaultPort, serverName);
  }

  @Override
  protected CommandManager createCommandManager() {
    return new SearchCommandManager();
  }

  @Override
  protected ExecutorService createExecutorService() {
    return Executors.newCachedThreadPool();
  }

  public static void main(String[] args) {
    SearchServer ss = new SearchServer(SEARCH_PORT, "SearchServer");
    ss.runServer(args);
  }
}
