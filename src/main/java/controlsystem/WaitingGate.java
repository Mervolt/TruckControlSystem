package controlsystem;

public class WaitingGate implements IGate {
    private Truck processedTruck;
    private int timeInProcess;

    @Override
    public int getTimeInProcess() {
        return timeInProcess;
    }

    public void incrementTimeInProcess() {
        timeInProcess++;
    }

    @Override
    public Truck getProcessedTruck() {
        return processedTruck;
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
