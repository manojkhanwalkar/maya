package util;

import data.CcvPin;
import data.PinPii;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolrClient<T> {

    final HttpSolrClient solr;

     SolrClient(String url, String collectionName) {

         String urlString = url+collectionName;
          solr = new HttpSolrClient.Builder(urlString).build();
         solr.setParser(new XMLResponseParser());
    }


    public void add(T t)
    {
        try {
            solr.addBean(t);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
    }

    public void commit()
    {
        try {
            solr.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public<T> List<T> query(String queryStr, Class<T> clazz)
    {
        List<T> list = new ArrayList<>();

        try {
            SolrQuery query = new SolrQuery();

            query.set("q", queryStr);
            QueryResponse response = solr.query(query);

            SolrDocumentList docList = response.getResults();


            for (SolrDocument doc : docList) {

                T t = (T)JSONUtil.fromJSON(doc.jsonStr(),clazz);
                list.add(t);

            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;


    }


    public<T> List<T> query(Map<String,String> queryParamMap, Class<T> clazz)
    {
        List<T> list = new ArrayList<>();
        try {
            MapSolrParams queryParams = new MapSolrParams(queryParamMap);

            final QueryResponse response2 = solr.query(queryParams);
            final SolrDocumentList documents = response2.getResults();

            for (SolrDocument doc : documents) {


                T t = (T)JSONUtil.fromJSON(doc.jsonStr(),clazz);
                list.add(t);


            }

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }


}
