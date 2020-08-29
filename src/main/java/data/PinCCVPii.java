package data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.solr.client.solrj.beans.Field;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PinCCVPii {

    String ccv;
    int ein;

    String FName;
    String LName;


    public String getFName() {
        return FName;
    }

    @Field("firstName")
    @JsonProperty("firstName")
    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    @Field("lastName")
    @JsonProperty("lastName")
    public void setLName(String LName) {
        this.LName = LName;
    }




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

    @Override
    public String toString() {
        return "PinCCVPii{" +
                "ccv='" + ccv + '\'' +
                ", ein=" + ein +
                ", FName='" + FName + '\'' +
                ", LName='" + LName + '\'' +
                '}';
    }
}
