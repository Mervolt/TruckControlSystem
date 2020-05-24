package controlsystem;

public class WaitingGate implements IGate {
    private Truck processedTruck;

    @Override
    public Truck getProcessedTruck() {
        return processedTruck;
    }

    @Override
    public void processStep() {

    }

    @Override
    public void releaseTruck() {
        this.processedTruck = null;
    }

    @Override
    public void placeTruck(Truck truck) {
        this.processedTruck = truck;
    }

    @Override
    public boolean isEmpty() {
        return processedTruck == null;
    }
}
