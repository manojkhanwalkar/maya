package data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.solr.client.solrj.beans.Field;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PinPii {


    int ein;

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

                ", FName='" + FName + '\'' +
                ", LName='" + LName + '\'' +
                '}';
    }
}
