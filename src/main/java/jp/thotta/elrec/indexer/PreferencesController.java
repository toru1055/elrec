package jp.thotta.elrec.indexer;

import jp.thotta.elrec.common.ItemUsersPreferencesList;
import jp.thotta.elrec.common.UserItemsPreferencesList;

public class PreferencesController {
  private ItemUsersPreferencesList itemUsers;
  private UserItemsPreferencesList userItems;

  public PreferencesController() {
    itemUsers = new ItemUsersPreferencesList();
    userItems = new UserItemsPreferencesList();
  }

  public void addPreference(long userId, long itemId) {
    itemUsers.add(itemId, userId);
    userItems.add(userId, itemId);
  }

  public void delPreference(long userId, long itemId) {
    itemUsers.del(itemId, userId);
    userItems.del(userId, itemId);
  }

}
