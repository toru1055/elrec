package jp.thotta.elrec.common;

import java.util.HashMap;

public class BasePreferences {
  private long id;
  private HashMap<Long,Boolean> prefer_ids;

  public BasePreferences(long id, HashMap<Long,Boolean> prefer_ids) {
    this.id = id;
    this.prefer_ids = prefer_ids;
  }

  public BasePreferences(long id, String prefer_ids_csv) {
    this(id, csv2ids(prefer_ids_csv));
  }

  public HashMap<Long,Boolean> getPreferenceIds() {
    return this.prefer_ids;
  }

  public void addPreference(long prefer_id) {
    this.prefer_ids.put(prefer_id, true);
  }

  public long getId() {
    return this.id;
  }

  public String getPreferenceIdsCsv() {
    String prefs_csv = "";
    String delim = "";
    for(Long pid : this.prefer_ids.keySet()) {
      prefs_csv += delim + pid.toString();
      delim = ",";
    }
    return prefs_csv;
  }

  private static HashMap<Long,Boolean> csv2ids(String ids_csv) {
    HashMap<Long,Boolean> ids = new HashMap<Long,Boolean>();
    String[] ids_str = ids_csv.split(",", 0);
    for(int i = 0; i < ids_str.length; i++) {
      ids.put(Long.parseLong(ids_str[i]), true);
    }
    return ids;
  }
}
