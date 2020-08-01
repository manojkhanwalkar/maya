package data;

public class VaultPutRequest {

    String requestId;

    String ccNumber;

    String merchantId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @Override
    public String toString() {
        return "VaultPutRequest{" +
                "requestId='" + requestId + '\'' +
                ", ccNumber='" + ccNumber + '\'' +
                ", merchantId='" + merchantId + '\'' +
                '}';
    }
}
