package data;

public class PMResponse {


    String score;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    static PMResponse EMPTY = new PMResponse();

    public static PMResponse getEMPTY()
    {
        return EMPTY;
    }


}
