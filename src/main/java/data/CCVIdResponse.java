package data;

public class CCVIdResponse {

    ResponseHeader responseHeader;

    VelocityStatsList velocityStatsList;

    String score ;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public VelocityStatsList getVelocityStatsList() {
        return velocityStatsList;
    }

    public void setVelocityStatsList(VelocityStatsList velocityStatsList) {
        this.velocityStatsList = velocityStatsList;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
