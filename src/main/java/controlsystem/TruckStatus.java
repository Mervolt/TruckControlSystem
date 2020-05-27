package controlsystem;

public class TruckStatus implements Comparable<TruckStatus> {
    private String truckId;
    private Integer estimatedTime;
    TruckPosition position;

    public TruckStatus(String truckId, int estimatedTime, TruckPosition position) {
        this.truckId = truckId;
        this.estimatedTime = estimatedTime;
        this.position = position;
    }

    @Override
    public String toString() {
        return "TruckStatus{" +
                "truckId='" + truckId + '\'' +
                ", estimatedTime=" + estimatedTime +
                ", position=" + position +
                '}';
    }

    @Override
    public int compareTo(TruckStatus o) {
        return this.estimatedTime.compareTo(o.estimatedTime);
    }
}
