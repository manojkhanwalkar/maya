package data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.solr.client.solrj.beans.Field;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PinPii {


    int ein;
    int tin;
    String FName;
    String LName;


    public int getEin() {
        return ein;
    }

    @Field("id")
    @JsonProperty("id")
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


    @Override
    public String toString() {
        return "PinPii{" +
                "ein=" + ein +
                ", tin=" + tin +
                ", FName='" + FName + '\'' +
                ", LName='" + LName + '\'' +
                '}';
    }
}
