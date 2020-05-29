package controlsystem;

public class WaitingGate implements IGate {
    private Truck processedTruck;
    private int timeInProcess;

    /**
     *
     * @return current value of processed time
     */
    @Override
    public int getTimeInProcess() {
        return timeInProcess;
    }

    /**
     * increments value in time process
     */
    public void incrementTimeInProcess() {
        timeInProcess++;
    }

    /**
     *
     * @return truck currently processed in gate
     */
    @Override
    public Truck getProcessedTruck() {
        return processedTruck;
    }

    /**
     * method is responsible for releasing truck from gate, so the new one can be placed
     */
    @Override
    public void releaseTruck() {
        this.processedTruck = null;
        this.timeInProcess = 0;
    }

    /**
     * method is responsible for placing truck in gate
     * @param truck to be placed in gate
     */
    @Override
    public void placeTruck(Truck truck) {
        this.processedTruck = truck;
        this.timeInProcess = 0;
    }

    /**
     * method is responsible for checking if waiting gate has no truck in itself
     * @return boolean whether gate is empty and new truck can be placed in
     */
    @Override
    public boolean isEmpty() {
        return processedTruck == null;
    }
}
