package controlsystem;

public class ControlGate implements IGate {
    private Truck processedTruck;
    private int timeInProcess;

    public ControlGate() {
        this.timeInProcess = 0;
    }

    public int getTimeInProcess() {
        return timeInProcess;
    }

    @Override
    public Truck getProcessedTruck() {
        return processedTruck;
    }

    @Override
    public void processStep(){
        this.timeInProcess++;
        if(timeInProcess == processedTruck.getWeightAmount())
            releaseTruck();
    }

    @Override
    public void releaseTruck() {
        this.processedTruck = null;
        timeInProcess = 0;
    }

    @Override
    public void placeTruck(Truck truck) {
        this.processedTruck = truck;
        timeInProcess = 0;
    }

    @Override
    public boolean isEmpty() {
        return processedTruck == null;
    }
}
