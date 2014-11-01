package jp.thotta.elrec.common;

import redis.clients.jedis.Jedis;

public class JedisFactory {
  private static Jedis jedis;
  
  private JedisFactory(){}
  
  public static Jedis getInstance(){
    if(jedis==null){
      jedis=new Jedis("localhost");
    }
    return jedis;
  }
}
