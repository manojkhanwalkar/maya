package data;

public class Transaction {

    String merchantId;
    double amount;
    String transactionTime;
    String description;

    public Transaction(String merchantId, double amount, String transactionTime) {
        this.merchantId = merchantId;
        this.amount = amount;
        this.transactionTime = transactionTime;
    }


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "merchantId='" + merchantId + '\'' +
                ", amount=" + amount +
                ", transactionTime='" + transactionTime + '\'' +
                ", description='" + description + '\'' +
                "}\n";
    }
}
