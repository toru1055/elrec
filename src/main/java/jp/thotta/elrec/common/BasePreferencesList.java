package jp.thotta.elrec.common;

import java.util.HashMap;
import redis.clients.jedis.Jedis;

public abstract class BasePreferencesList {
  private Jedis jedis = JedisFactory.getInstance();

  public void add(long sourceId, long targetId) {
    String csv = this.jedis.get(this.getKeyName(sourceId));
    BasePreferences p = new BasePreferences(sourceId, csv);
    p.addPreference(targetId);
    this.jedis.set(this.getKeyName(sourceId), p.getPreferenceIdsCsv());
  }

  public HashMap<Long,Boolean> getPreferenceIds(long sourceId) {
    String csv = this.jedis.get(this.getKeyName(sourceId));
    BasePreferences p = new BasePreferences(sourceId, csv);
    return p.getPreferenceIds();
  }

  public void deleteKey(long sourceId) {
    this.jedis.del(this.getKeyName(sourceId));
  }

  private String getKeyName(long sourceId) {
    return this.getKeyBase() + ":" + Long.toString(sourceId);
  }

  protected abstract String getKeyBase();
}
