package jp.thotta.elrec.searcher;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import jp.thotta.elrec.common.CommandManager;

public class SearchCommandManager implements CommandManager {
  private Gson fGson = new Gson();
  private ItemBasedRecommender fRecommender = new ItemBasedRecommender();

  @Override
  public String execute(String jsonCommand) {
    String resJson = null;
    try {
      SearchCommand searchCommand = fGson.fromJson(jsonCommand, SearchCommand.class);
      resJson = fGson.toJson(searchCommand.execCommand(fRecommender));
    } catch(JsonSyntaxException e) {
    }
    return resJson;
  }
}
