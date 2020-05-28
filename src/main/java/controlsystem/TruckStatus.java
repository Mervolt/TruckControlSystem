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

    /**
     *
     * @param o truckStatus to be compared with
     * @return > 0 if this has grater estimated time
     * 0 if equal
     * < 0 if estimated time for this is shorter than o
     */
    @Override
    public int compareTo(TruckStatus o) {
        return this.estimatedTime.compareTo(o.estimatedTime);
    }
}
