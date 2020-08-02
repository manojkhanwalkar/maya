package util;

import data.Token;

public class HashCardTuple extends CardTuple{

    String salt;

    String pan;



    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }


    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
