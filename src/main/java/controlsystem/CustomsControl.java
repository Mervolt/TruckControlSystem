package controlsystem;

import java.util.LinkedList;
import java.util.List;

public class CustomsControl {
    private ControlLane firstLane;
    private ControlLane secondLane;
    private WaitingLane waitingLane;
    private int truckCounter;
    private int immovableTrucks = 1;
    private CustomsControlStatusPrinter printer;

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
        if(isControlLaneAvailable())
            return selectQuickestAvailableControlLane();
        else
            return waitingLane;
    }

    private boolean isControlLaneAvailable(){
        return firstLane.hasFreePlace() || secondLane.hasFreePlace();
    }

    private ControlLane selectQuickestAvailableControlLane(){
        if(firstLane.hasFreePlace() && secondLane.hasFreePlace())
            return selectQuickerControlLane();
        else if(!firstLane.hasFreePlace() && secondLane.hasFreePlace())
            return secondLane;
        else
            return firstLane;
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
        printer = new CustomsControlStatusPrinter(firstLane, secondLane, waitingLane);
        printer.print();
    }

    public void step(){
        firstLane.processStep();
        secondLane.processStep();
        int freeSlots = countFreeSlots();
        boolean waitingGateTruckMoved = false;
        if(canTruckFromGateMove(freeSlots)) {
            moveTruckFromGate();
            waitingGateTruckMoved = true;
        }
        if(canTruckFromLaneMove(freeSlots))
            moveFirstTruckFromLane();
        if(waitingGateTruckMoved)
            waitingLane.processStep();
        optimizeRoute();
    }

    public int countFreeSlots(){
        int controlLanesTrucks = firstLane.getTrucksAmount() + secondLane.getTrucksAmount();
        int controlLanesCapacity = firstLane.getLaneCapacity() + secondLane.getLaneCapacity();
        return controlLanesCapacity - controlLanesTrucks;
    }

    protected boolean canTruckFromGateMove(int freeSlots){
        return (freeSlots >= 1 && !waitingLane.getGate().isEmpty());
    }

    protected boolean canTruckFromLaneMove(int freeSlots){
        return (freeSlots == 2 && waitingLane.getTrucksAmount() > 0);
    }

    private void moveTruckFromGate(){
        Truck movingTruck = waitingLane.getGate().getProcessedTruck();
        selectQuickestAvailableControlLane().placeArrivingTruck(movingTruck);
        waitingLane.getGate().releaseTruck();
    }

    private void moveFirstTruckFromLane(){
        Truck truckFromLane = waitingLane.getQueue().poll();
        selectQuickestAvailableControlLane().placeArrivingTruck(truckFromLane);
    }

    private void optimizeRoute(){
        TruckPlacement placement = countTruckPlacement();
        if(placement != TruckPlacement.balanced)
            reorganizeTrucks(placement);
    }

    private TruckPlacement countTruckPlacement() {
        int firstLaneTrucks = firstLane.getTrucksAmount();
        int secondLaneTrucks = secondLane.getTrucksAmount();
        TruckPlacement placement;
        if(firstLaneTrucks > secondLaneTrucks)
            placement = TruckPlacement.firstLaneSided;
        else if(secondLaneTrucks > firstLaneTrucks)
            placement = TruckPlacement.secondLaneSided;
        else
            placement = TruckPlacement.balanced;
        return placement;
    }

    private void reorganizeTrucks(TruckPlacement placement) {
        ControlLane shorterLane;
        ControlLane longerLane;
        if(placement == TruckPlacement.firstLaneSided){
            shorterLane = secondLane;
            longerLane = firstLane;
        }
        else{
            shorterLane = firstLane;
            longerLane = secondLane;
        }
        moveLighterTrucksToShorterLane(shorterLane, longerLane);
    }

    private void moveLighterTrucksToShorterLane(ControlLane shorterLane, ControlLane longerLane) {
        List<Truck> shorterLaneQueue = shorterLane.getQueue();
        List<Truck> longerLaneQueue = longerLane.getQueue();
        for(int truckIterator = immovableTrucks; truckIterator < shorterLane.getQueue().size(); truckIterator++){
            if(trucksShouldBeSwitched(shorterLaneQueue.get(truckIterator), longerLaneQueue.get(truckIterator))){
                switchTrucksBetweenLanes(shorterLaneQueue, longerLaneQueue, truckIterator);
            }
        }
    }

    private boolean trucksShouldBeSwitched(Truck shorterLaneTruck, Truck longerLaneTruck){
        return  longerLaneTruck.isLighter(shorterLaneTruck);
    }

    private void switchTrucksBetweenLanes(List<Truck> shorterLaneQueue, List<Truck> longerLaneQueue, int place) {
        Truck temporaryTruck = shorterLaneQueue.get(place);
        shorterLaneQueue.add(place, longerLaneQueue.get(place));
        longerLaneQueue.add(place, temporaryTruck);
    }

}
