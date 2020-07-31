package data;

import java.util.ArrayList;

public class VelocityStatsList {

   ArrayList<VelocityStats> stats = new ArrayList<>();

    public ArrayList<VelocityStats> getStats() {
        return stats;
    }

    public void setStats(ArrayList<VelocityStats> stats) {
        this.stats = stats;
    }

    public void addStats(VelocityStats vs)
    {
        stats.add(vs);
    }

    @Override
    public String toString() {
        return "VelocityStatsList{" +
                "stats=" + stats +
                '}';
    }

    static VelocityStatsList EMPTY = new VelocityStatsList();

    public static VelocityStatsList getEMPTY()
    {
        return EMPTY;
    }
}
