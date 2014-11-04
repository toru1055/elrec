package jp.thotta.elrec.searcher;
import java.util.List;
import java.util.ArrayList;

public class SearchCommand {
  private static final String INPUT_TYPE_ITEM_ID_LIST = "item_id_list";
  private static final String INPUT_TYPE_ITEM_ID = "item_id";
  private static final String INPUT_TYPE_USER_ID = "user_id";
  private String inputType;
  private Long userId;
  private Long itemId;
  private Integer howMany;
  private Boolean includeKnownItems;
  private List<Long> itemIdList;

  private boolean isCorrectFormat() {
    boolean isCorrect = false;
    if(inputType != null && 
       includeKnownItems != null && 
       howMany != null) {
      if(inputType.equals(INPUT_TYPE_ITEM_ID_LIST)) {
        if(itemIdList != null) {
          isCorrect = true;
        }
      } else if(inputType.equals(INPUT_TYPE_ITEM_ID)) {
        if(itemId != null) {
          isCorrect = true;
        }
      } else if(inputType.equals(INPUT_TYPE_USER_ID)) {
        if(userId != null) {
          isCorrect = true;
        }
      }
    }
    return isCorrect;
  }

  public List<RecommendedItem> execCommand(ItemBasedRecommender r) {
    List<RecommendedItem> rItems = null;
    if(this.isCorrectFormat() == true) {
      if(inputType.equals(INPUT_TYPE_ITEM_ID_LIST)) {
        rItems = r.mostSimilarItems(itemIdList, howMany, includeKnownItems);
      } else if(inputType.equals(INPUT_TYPE_ITEM_ID)) {
        rItems = r.mostSimilarItems(itemId, howMany, includeKnownItems);
      } else if(inputType.equals(INPUT_TYPE_USER_ID)) {
        rItems = r.recommend(userId, howMany, includeKnownItems);
      } else {
        // Todo: make error object using SearchResult.class
      }
    }
    return rItems;
  }
}
