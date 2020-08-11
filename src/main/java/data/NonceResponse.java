package data;

import java.util.ArrayList;

public class NonceResponse {

    ArrayList<String> nonces = new ArrayList<>();

    public ArrayList<String> getNonces() {
        return nonces;
    }

    public void setNonces(ArrayList<String> nonces) {
        this.nonces = nonces;
    }

    public void add(String nonce)
    {
        nonces.add(nonce);
    }


    @Override
    public String toString() {
        return "NonceResponse{" +
                "nonces=" + nonces +
                '}';
    }
}
