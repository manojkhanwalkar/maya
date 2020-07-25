package vfs;

public class VelocityStats {


    String type;

    String value;

    CurrentCount currentCount;

    HistoricalCount historicalCount;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HistoricalCount getHistoricalCount() {
        return historicalCount;
    }

    public void setHistoricalCount(HistoricalCount historicalCount) {
        this.historicalCount = historicalCount;
    }

    public CurrentCount getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(CurrentCount currentCount) {
        this.currentCount = currentCount;
    }

    @Override
    public String toString() {
        return "VelocityStats{" +
                "historicalCount=" + historicalCount +
                ", currentCount=" + currentCount +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
