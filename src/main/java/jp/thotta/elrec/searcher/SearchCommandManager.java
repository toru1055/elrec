package jp.thotta.elrec.searcher;

import com.google.gson.Gson;

import jp.thotta.elrec.common.CommandManager;

public class SearchCommandManager implements CommandManager {
  private Gson fGson = new Gson();
  private ItemBasedRecommender fRecommender = new ItemBasedRecommender();

  @Override
  public String execute(String jsonCommand) {
    SearchCommand searchCommand = fGson.fromJson(jsonCommand, SearchCommand.class);
    String resJson = fGson.toJson(searchCommand.execCommand(fRecommender));
    return resJson;
  }
}
