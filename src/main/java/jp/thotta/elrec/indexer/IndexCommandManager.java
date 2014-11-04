package jp.thotta.elrec.indexer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import jp.thotta.elrec.common.CommandManager;

public class IndexCommandManager implements CommandManager {
  private Gson fGson = new Gson();
  private PreferenceIndexer fPrefIndexer = new PreferenceIndexer();

  @Override
  public String execute(String jsonCommand) {
    String resJson = null;
    try {
      IndexCommand indexCommand = fGson.fromJson(jsonCommand, IndexCommand.class);
      if(indexCommand.execCommand(fPrefIndexer)) {
        resJson = "OK";
      }
    } catch(JsonSyntaxException e) {
    }
    return resJson;
  }
}
