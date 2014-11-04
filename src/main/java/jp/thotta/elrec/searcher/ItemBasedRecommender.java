package jp.thotta.elrec.searcher;

import jp.thotta.elrec.common.ItemUsersPreferencesList;
import jp.thotta.elrec.common.UserItemsPreferencesList;

import java.lang.Math;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ItemBasedRecommender {
  private ItemUsersPreferencesList itemUsers;
  private UserItemsPreferencesList userItems;

  public ItemBasedRecommender() {
    itemUsers = new ItemUsersPreferencesList();
    userItems = new UserItemsPreferencesList();
  }

  public List<RecommendedItem> recommend(long userId, int howMany,
                                         boolean includeKnownItems) {
    Map<Long,Boolean> itemIds = userItems.getPreferenceIds(userId);
    List<Long> itemIdList = new ArrayList<Long>(itemIds.keySet());
    return mostSimilarItems(itemIdList, howMany, includeKnownItems);
  }

  public List<RecommendedItem> mostSimilarItems(long itemId, int howMany,
                                                boolean includeKnownItems) {
    List<Long> itemIdList = new ArrayList<Long>();
    itemIdList.add(itemId);
    return mostSimilarItems(itemIdList, howMany, includeKnownItems);
  }

  public List<RecommendedItem> mostSimilarItems(List<Long> itemIdList,
                                                int howMany,
                                                boolean includeKnownItems) {
    Map<Long,Double> userScores = calcUserScores(itemIdList);
    Map<Long,Double> itemScores = calcItemScores(userScores);
    return getTopItems(itemScores, itemIdList, howMany, includeKnownItems);
  }

  private Map<Long,Double> calcUserScores(List<Long> itemIdList) {
    Map<Long,Double> userScores = new HashMap<Long,Double>();
    for(Long itemId : itemIdList) {
      Map<Long,Boolean> userIds = itemUsers.getPreferenceIds(itemId);
      for(Long userId : userIds.keySet()) {
        Double uScore = userScores.get(userId);
        double additionalScore = 1.0 / Math.sqrt(userIds.size());
        //double additionalScore = 1.0;
        if(uScore != null) {
          userScores.put(userId, uScore.doubleValue() + additionalScore);
        } else {
          userScores.put(userId, additionalScore);
        }
      }
    }
    return userScores;
  }

  private Map<Long,Double> calcItemScores(Map<Long,Double> userScores) {
    Map<Long,Double> itemScores = new HashMap<Long,Double>();
    for(Long userId : userScores.keySet()) {
      Map<Long,Boolean> itemIds = userItems.getPreferenceIds(userId);
      for(Long itemId : itemIds.keySet()) {
        Double iScore = itemScores.get(itemId);
        double additionalScore = userScores.get(userId) / Math.sqrt(itemIds.size());
        //double additionalScore = userScores.get(userId) / itemIds.size();
        if(iScore != null) {
          itemScores.put(itemId, iScore.doubleValue() + additionalScore);
        } else {
          itemScores.put(itemId, additionalScore);
        }
      }
    }
    return itemScores;
  }

  private static List<RecommendedItem> getTopItems(
      Map<Long,Double> itemScores,
      List<Long> sourceItemIds,
      int howMany,
      boolean includeKnownItems) {
    List<RecommendedItem> topItems = new ArrayList<RecommendedItem>();
    List<RecommendedItem> orgItems = new ArrayList<RecommendedItem>();
    for(Long itemId : itemScores.keySet()) {
      if(includeKnownItems || sourceItemIds.indexOf(itemId) == -1) {
        orgItems.add(new RecommendedItem(itemId, itemScores.get(itemId)));
      }
    }
    sortRecommendedItemDesc(orgItems);
    for(RecommendedItem rItem : orgItems) {
      topItems.add(rItem);
      if(topItems.size() >= howMany) {
        break;
      }
    }
    return topItems;
  }

  private static void sortRecommendedItemDesc(
      List<RecommendedItem> rItemList) {
    Collections.sort(rItemList, new Comparator<RecommendedItem>() {
      @Override
      public int compare(RecommendedItem r1, RecommendedItem r2) {
        double s1 = r1.getScore();
        double s2 = r2.getScore();
        if(s2 > s1) {
          return 1;
        } else if(s1 == s2) {
          return 0;
        } else {
          return -1;
        }
      }
    });
  }
}
