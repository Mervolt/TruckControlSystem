package controlsystem;

public class Truck {
    private int weightAmount;
    private String truckId;

    public Truck(int weightAmount, String truckId) {
        this.weightAmount = weightAmount;
        this.truckId = truckId;
    }

    public int getWeightAmount() {
        return weightAmount;
    }

    public String getTruckId() {
        return truckId;
    }

    /**
     *
     * @param truck to be compared with this
     * @return true if this is lighter than truck
     */
    public boolean isLighter(Truck truck){
        return this.getWeightAmount() < truck.getWeightAmount();
    }
}
