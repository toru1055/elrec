package jp.thotta.elrec.indexer;

public class IndexCommand {
  private Long userId;
  private Long itemId;

  private boolean isCorrectFormat() {
    boolean isCorrect = false;
    if(userId != null && itemId != null) {
      isCorrect = true;
    }
    return isCorrect;
  }

  public boolean execCommand(PreferenceIndexer p) {
    if(this.isCorrectFormat() == true) {
      p.addPreference(userId, itemId);
      return true;
    } else {
      return false;
    }
  }
}
