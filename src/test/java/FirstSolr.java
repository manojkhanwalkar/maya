import data.CcvPin;
import data.PinPii;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import util.JSONUtil;

import static org.junit.Assert.assertEquals;

public class FirstSolr {

    public static void main(String[] args)  throws Exception {

        String urlString = "http://localhost:8983/solr/ccvpin";
        HttpSolrClient solr = new HttpSolrClient.Builder(urlString).build();
        solr.setParser(new XMLResponseParser());

        String urlString1 = "http://localhost:8983/solr/pinpii";
        HttpSolrClient solr1 = new HttpSolrClient.Builder(urlString1).build();
        solr1.setParser(new XMLResponseParser());

/*        SolrInputDocument document = new SolrInputDocument();
        document.addField("test", "123456");
        document.addField("name", "Kenmore Dishwasher");
        document.addField("price", "599.99");
        solr.add(document);
        solr.commit();

        solr.addBean( new ProductBean("888", "Apple iPhone 6s", "299.99") );
        solr.addBean( new ProductBean("889", "Apple iPhone 7s", "399.99") );
        solr.commit(); */

      /*  CcvPin cp = new CcvPin();
        cp.setCcv("C1");
        cp.setEin("E1");
        cp.setTin("T1");

        solr.addBean(cp);

        cp.setCcv("C1");
        cp.setEin("E2");
        cp.setTin("T2");

        solr.addBean(cp);

        cp.setCcv("C2");
        cp.setEin("E2");
        cp.setTin("T2");

        solr.addBean(cp);

        cp.setCcv("C2");
        cp.setEin("E3");
        cp.setTin("T3");

        solr.addBean(cp);

        solr.commit();*/

        SolrQuery query = new SolrQuery();


        query.set("q", "ccv:C1");
        QueryResponse response = solr.query(query);

        SolrDocumentList docList = response.getResults();

        int count=0;

        for (SolrDocument doc : docList) {

            System.out.println(doc.jsonStr());

            CcvPin cp1 = (CcvPin)JSONUtil.fromJSON(doc.jsonStr(),CcvPin.class);

            PinPii pp = new PinPii();
            pp.setEin(cp1.getEin());
            pp.setTin(cp1.getTin());

            pp.setFName("FN"+count);
            pp.setLName("LN" + count);

            solr1.addBean(pp);

        }

        solr1.commit();

        {
            SolrQuery query1 = new SolrQuery();

            query1.set("q", "ein:E1");
            QueryResponse response1 = solr.query(query1);

            SolrDocumentList docList1 = response1.getResults();

            for (SolrDocument doc : docList1)
            {
                System.out.println(doc.jsonStr());
                PinPii pp = (PinPii) JSONUtil.fromJSON(doc.jsonStr(),PinPii.class);


            }

        }





      /*  SolrDocument doc = solr.getById("888");
        System.out.println(doc.jsonStr()); */

    }


}
