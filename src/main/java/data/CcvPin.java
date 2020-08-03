package data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.solr.client.solrj.beans.Field;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CcvPin {

    String ccv;
    String ein;
    String tin;


    public String getCcv() {
        return ccv;
    }

    @Field("ccv")
    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    public String getEin() {
        return ein;
    }

    @Field("ein")
    public void setEin(String ein) {
        this.ein = ein;
    }

    public String getTin() {
        return tin;
    }

    @Field("tin")
    public void setTin(String tin) {
        this.tin = tin;
    }


}
