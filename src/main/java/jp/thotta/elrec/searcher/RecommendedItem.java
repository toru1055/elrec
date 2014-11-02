package jp.thotta.elrec.searcher;

public class RecommendedItem {
  private long itemId;
  private double score;

  public RecommendedItem(long itemId, double score) {
    this.itemId = itemId;
    this.score = score;
  }

  public long getItemId() {
    return itemId;
  }

  public double getScore() {
    return score;
  }
}
