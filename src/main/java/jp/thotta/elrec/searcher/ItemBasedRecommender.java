package jp.thotta.elrec.searcher;

import jp.thotta.elrec.common.ItemUsersPreferencesList;
import jp.thotta.elrec.common.UserItemsPreferencesList;

import java.lang.Math;
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
    return new ArrayList<RecommendedItem>();
  }

  public List<RecommendedItem> mostSimilarItems(long itemId, int howMany,
                                                boolean includeKnownItems) {
    return new ArrayList<RecommendedItem>();
  }

  public List<RecommendedItem> mostSimilarItems(ArrayList<Long> itemIdList,
                                                int howMany,
                                                boolean includeKnownItems) {
    HashMap<Long,Double> userScores = calcUserScores(itemIdList);
    HashMap<Long,Double> itemScores = calcItemScores(userScores);
    return getTopItems(itemScores, itemIdList, howMany, includeKnownItems);
  }

  private HashMap<Long,Double> calcUserScores(ArrayList<Long> itemIdList) {
    HashMap<Long,Double> userScores = new HashMap<Long,Double>();
    for(Long itemId : itemIdList) {
      HashMap<Long,Boolean> userIds = itemUsers.getPreferenceIds(itemId);
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

  private HashMap<Long,Double> calcItemScores(HashMap<Long,Double> userScores) {
    HashMap<Long,Double> itemScores = new HashMap<Long,Double>();
    for(Long userId : userScores.keySet()) {
      HashMap<Long,Boolean> itemIds = userItems.getPreferenceIds(userId);
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
      HashMap<Long,Double> itemScores,
      ArrayList<Long> sourceItemIds,
      int howMany,
      boolean includeKnownItems) {
    ArrayList<RecommendedItem> topItems = new ArrayList<RecommendedItem>();
    ArrayList<RecommendedItem> orgItems = new ArrayList<RecommendedItem>();
    for(Long itemId : itemScores.keySet()) {
      if(!includeKnownItems && sourceItemIds.indexOf(itemId) != -1) {
        continue;
      }
      orgItems.add(new RecommendedItem(itemId, itemScores.get(itemId)));
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
      ArrayList<RecommendedItem> rItemList) {
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
