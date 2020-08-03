package data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.solr.client.solrj.beans.Field;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PinPii {


    String ein;
    String tin;
    String FName;
    String LName;




    public String getEin() {
        return ein;
    }

    @Field("id")
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

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }
}
