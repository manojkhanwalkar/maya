package data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.solr.client.solrj.beans.Field;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CcvPin {

    String ccv;
    int ein;
    int tin;


    public String getCcv() {
        return ccv;
    }

    @Field("ccv")
    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    public int getEin() {
        return ein;
    }

    @Field("ein")

    public void setEin(int ein) {
        this.ein = ein;
    }

    public int getTin() {
        return tin;
    }

    @Field("tin")
    public void setTin(int tin) {
        this.tin = tin;
    }


    @Override
    public String toString() {
        return "CcvPin{" +
                "ccv='" + ccv + '\'' +
                ", ein=" + ein +
                ", tin=" + tin +
                '}';
    }
}
