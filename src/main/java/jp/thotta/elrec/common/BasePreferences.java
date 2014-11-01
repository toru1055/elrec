package jp.thotta.elrec.common;

import java.util.HashMap;

public class BasePreferences {
  private long id;
  private HashMap<Long,Boolean> preferenceIds;

  public BasePreferences(long id, HashMap<Long,Boolean> preferenceIds) {
    this.id = id;
    this.preferenceIds = preferenceIds;
  }

  public BasePreferences(long id, String preferenceIdsCsv) {
    this(id, csv2ids(preferenceIdsCsv));
  }

  public HashMap<Long,Boolean> getPreferenceIds() {
    return this.preferenceIds;
  }

  public void addPreference(long prefer_id) {
    this.preferenceIds.put(prefer_id, true);
  }

  public void delPreference(long prefer_id) {
    this.preferenceIds.remove(prefer_id);
  }

  public long getId() {
    return this.id;
  }

  public String getPreferenceIdsCsv() {
    String PreferenceIdsCsv = "";
    String delim = "";
    for(Long pid : this.preferenceIds.keySet()) {
      PreferenceIdsCsv += delim + pid.toString();
      delim = ",";
    }
    return PreferenceIdsCsv;
  }

  private static HashMap<Long,Boolean> csv2ids(String ids_csv) {
    HashMap<Long,Boolean> ids = new HashMap<Long,Boolean>();
    if(ids_csv != null) {
      String[] ids_str = ids_csv.split(",", 0);
      for(int i = 0; i < ids_str.length; i++) {
        ids.put(Long.parseLong(ids_str[i]), true);
      }
    }
    return ids;
  }
}
