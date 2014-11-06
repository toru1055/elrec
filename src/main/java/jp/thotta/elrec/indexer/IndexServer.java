package jp.thotta.elrec.indexer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.thotta.elrec.common.CommandManager;
import jp.thotta.elrec.common.AbstractServer;

public class IndexServer extends AbstractServer {
  public IndexServer(int port, String serverName) {
    super(port, serverName);
  }

  @Override
  protected CommandManager createCommandManager() {
    return new IndexCommandManager();
  }

  @Override
  protected ExecutorService createExecutorService() {
    return Executors.newSingleThreadExecutor();
  }

  public static void main(String[] args) {
    IndexServer is = new IndexServer(INDEX_PORT, "IndexServer");
    is.runServer(args);
  }
}
