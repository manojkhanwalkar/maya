package util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class SolrUtil {

   private static SolrUtil instance = new SolrUtil();

  public static SolrUtil getInstance()
  {
      return instance;
  }


  private SolrUtil() {}

  ConcurrentMap<String,SolrClient> client = new ConcurrentHashMap<>();

  public<T> void addClient(String url , String collectionName)
  {
      SolrClient<T> solrClient = new SolrClient<>(url,collectionName);

      client.put(collectionName,solrClient);


  }
public<T>  SolrClient<T> getClient(String collectionName)
{
    return client.get(collectionName);
}




}

