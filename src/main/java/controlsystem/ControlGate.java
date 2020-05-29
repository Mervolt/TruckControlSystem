package controlsystem;


public class ControlGate implements IGate {
    private Truck processedTruck;
    private int timeInProcess;

    /**
     * default constructor
     */
    public ControlGate() {
        this.timeInProcess = 0;
    }

    /**
     *
     * @return time current truck has already been in process
     */
    @Override
    public int getTimeInProcess() {
        return timeInProcess;
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
     * method is responsible for processing step for control gate
     * i.e. it invokes truck release and processes time
     */
    public void processStep(){
        this.timeInProcess++;
        if(processedTruck == null)
            return;
        if(timeInProcess == processedTruck.getWeightAmount())
            releaseTruck();
    }

    /**
     * method is responsible for releasing truck from gate, so the new one can be placed
     */
    @Override
    public void releaseTruck() {
        this.processedTruck = null;
        timeInProcess = 0;
    }

    /**
     * method is responsible for placing truck in gate
     * @param truck to be placed in gate
     */
    @Override
    public void placeTruck(Truck truck) {
        this.processedTruck = truck;
        timeInProcess = 0;
    }

    /**
     * method is responsible for checking if control gate has no truck in itself
     * @return boolean whether gate is empty and new truck can be placed in
     */
    @Override
    public boolean isEmpty() {
        return processedTruck == null;
    }
}
