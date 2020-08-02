package data;

public class Token {

    String value;

    public enum Type { FPE , FPE1 , Hash , HashF6L4};

     Type type;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
