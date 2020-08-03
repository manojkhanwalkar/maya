import org.apache.solr.client.solrj.beans.Field;

public class ProductBean {

    String id;
    String name;
    String price;

    String newfield = "Default value";

    long time;

    public long getTime() {
        return time;
    }

    @Field("time")
    public void setTime(long time) {
        this.time = time;
    }

    public String getNewfield() {
        return newfield;
    }

    public void setNewfield(String newfield) {
        this.newfield = newfield;
    }

    public ProductBean(String id, String name, String price) {
        this.id = id;
        this.name = name;
        this.price = price;

        time = System.nanoTime();
    }

    @Field("test")
    protected void setId(String id) {
        this.id = id;
    }

    @Field("name")
    protected void setName(String name) {
        this.name = name;
    }

    @Field("price")
    protected void setPrice(String price) {
        this.price = price;
    }


    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getPrice() {
        return price;
    }
}