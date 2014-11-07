package jp.thotta.elrec.indexer;

import jp.thotta.elrec.common.AbstractClient;

public class IndexClient extends AbstractClient {
  public IndexClient(int defaultPort, String clientName) {
    super(defaultPort, clientName);
  }

  public static void main(String[] args) {
    IndexClient ic = new IndexClient(INDEX_PORT, "IndexClient");
    ic.runClient(args);
  }
}
