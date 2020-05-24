package controlsystem;

public class CustomsControl {
    private ControlLane firstLane;
    private ControlLane secondLane;
    private WaitingLane waitingLane;
    private int truckCounter;

    public ControlLane getFirstLane() {
        return firstLane;
    }

    public ControlLane getSecondLane() {
        return secondLane;
    }

    public WaitingLane getWaitingLane() {
        return waitingLane;
    }

    public int getTruckCounter() {
        return truckCounter;
    }

    public CustomsControl() {
        this.firstLane = new ControlLane();
        this.secondLane = new ControlLane();
        this.waitingLane = new WaitingLane();
        this.truckCounter = 0;
    }

    public String arrive(int truckWeight){
        Truck arrivingTruck = new Truck(truckWeight, generateTruckIdAndIncrementCounter());
        if(!waitingLane.getGate().isEmpty())
            waitingLane.placeArrivingTruck(arrivingTruck);
        else {
            ILane quickestLane = selectQuickestAvailableLane();
            quickestLane.placeArrivingTruck(arrivingTruck);
        }
        return arrivingTruck.getTruckId();
    }

    private ILane selectQuickestAvailableLane() {
        if(firstLane.hasFreePlace() && secondLane.hasFreePlace())
            return selectQuickerControlLane();
        else if(!firstLane.hasFreePlace() && secondLane.hasFreePlace())
            return secondLane;
        else if(firstLane.hasFreePlace() && !secondLane.hasFreePlace())
            return firstLane;
        else
            return waitingLane;
    }

    private ControlLane selectQuickerControlLane() {
        if(firstLane.getGate().isEmpty())
            return firstLane;
        if(secondLane.getGate().isEmpty())
            return secondLane;
        int firstLaneProcessingTime = firstLane.getLaneProcessingTime();
        int secondLaneProcessingTime = secondLane.getLaneProcessingTime();
        return firstLaneProcessingTime > secondLaneProcessingTime ? secondLane : firstLane;
    }

    private String generateTruckIdAndIncrementCounter() {
        String truckId = "T-" + this.truckCounter;
        this.truckCounter++;
        return truckId;
    }

    public void printStatus(){

    }

    public void step(){
        firstLane.processStep();
        secondLane.processStep();
        waitingLane.processStep();
        if(hasFreeSlot()) {
            Truck movingTruck = waitingLane.getGate().getProcessedTruck();
            ControlLane targetLane = selectQuickerControlLane();
            targetLane.placeArrivingTruck(movingTruck);
            waitingLane.processStep();
        }
    }

    public boolean hasFreeSlot(){
        int controlLanesTrucks = firstLane.getTrucksAmount() + secondLane.getTrucksAmount();
        int controlLanesCapacity = firstLane.getLaneCapacity() + secondLane.getLaneCapacity();
        return controlLanesTrucks < controlLanesCapacity;
    }

}
