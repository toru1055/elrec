package jp.thotta.elrec.searcher;

import jp.thotta.elrec.common.AbstractClient;

public class SearchClient extends AbstractClient {
  public SearchClient(int defaultPort, String clientName) {
    super(defaultPort, clientName);
  }

  public static void main(String[] args) {
    SearchClient sc = new SearchClient(SEARCH_PORT, "SearchClient");
    sc.runClient(args);
  }
}
