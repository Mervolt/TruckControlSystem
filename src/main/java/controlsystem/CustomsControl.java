package controlsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class responsible for simulation of movement during customs control
 */
public class CustomsControl {
    private ControlLane firstLane;
    private ControlLane secondLane;
    private WaitingLane waitingLane;
    private int immovableTrucks = 1;
    private TruckGenerator truckGenerator;

    /**
     * default constructor
     */
    public CustomsControl() {
        this.firstLane = new ControlLane();
        this.secondLane = new ControlLane();
        this.waitingLane = new WaitingLane();
        this.truckGenerator = new TruckGenerator();
     }

    /**
     *
     * @return current truck counter
     */
    public int getTruckCounter(){
        return truckGenerator.getTruckCounter();
    }

    /**
     *
     * @return reference to first lane
     */
    public ControlLane getFirstLane() {
        return firstLane;
    }

    /**
     *
     * @return reference to second lane
     */
    public ControlLane getSecondLane() {
        return secondLane;
    }

    /**
     *
     * @return reference to waiting lane
     */
    public WaitingLane getWaitingLane() {
        return waitingLane;
    }


    /**
     * method responsible for creating truck with given weight and placing it in customs control
     * @param truckWeight weight of truck to arrive
     * @return truckId of newly generated, placed truck
     */
    public String arrive(int truckWeight){
        Truck arrivingTruck = truckGenerator.createTruck(truckWeight);
        if(!waitingLane.getGate().isEmpty())
            waitingLane.placeArrivingTruck(arrivingTruck);
        else {
            ILane quickestLane = selectQuickestAvailableLane();
            quickestLane.placeArrivingTruck(arrivingTruck);
        }
        return arrivingTruck.getTruckId();
    }

    /**
     *
     * @return control lane with lower processing time,
     * or waiting lane if no free places are available in control lanes
     */
    private ILane selectQuickestAvailableLane() {
        if(isControlLaneAvailable())
            return selectQuickestAvailableControlLane();
        else
            return waitingLane;
    }

    /**
     *
     * @return true if there is any free spot in any control lane
     */
    private boolean isControlLaneAvailable(){
        return firstLane.hasFreePlace() || secondLane.hasFreePlace();
    }

    /**
     * should be called only if there is guarantee of free place in control lane
     * use selectQuickestAvailableLane otherwise as designed
     * @return reference to quicker lane if lane is available
     */
    private ControlLane selectQuickestAvailableControlLane(){
        if(firstLane.hasFreePlace() && secondLane.hasFreePlace())
            return selectQuickerControlLane();
        else if(!firstLane.hasFreePlace() && secondLane.hasFreePlace())
            return secondLane;
        else
            return firstLane;
    }

    /**
     * should be called only if there is guarantee of free place in control lanes
     * use selectQuickestAvailableLane otherwise as designed
     * @return reference to quicker control lane
     */
    private ControlLane selectQuickerControlLane() {
        if(firstLane.getGate().isEmpty())
            return firstLane;
        if(secondLane.getGate().isEmpty())
            return secondLane;
        int firstLaneProcessingTime = firstLane.getLaneProcessingTime();
        int secondLaneProcessingTime = secondLane.getLaneProcessingTime();
        return firstLaneProcessingTime > secondLaneProcessingTime ? secondLane : firstLane;
    }

    /**
     * method responsible for visualization of control
     */
    public void printStatus(){
        CustomsControlStatusPrinter printer = new CustomsControlStatusPrinter(firstLane, secondLane, waitingLane);
        printer.print();
    }

    /**
     * method responsible for processing a one time unit step on the simulation
     */
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
        if(truckGenerator.isGenerationEnabled())
            generateTruck();
        optimizeRoute();
    }

    /**
     * @return free slots in control lanes and gates
     */
    public int countFreeSlots(){
        int controlLanesTrucks = firstLane.getTrucksAmount() + secondLane.getTrucksAmount();
        if(!firstLane.getGate().isEmpty())
            controlLanesTrucks++;
        if(!secondLane.getGate().isEmpty())
            controlLanesTrucks++;
        /*+2 for control gates*/
        int controlLanesCapacity = firstLane.getLaneCapacity() + secondLane.getLaneCapacity() + 2;
        return controlLanesCapacity - controlLanesTrucks;
    }

    /**
     *
     * @param freeSlots free slots in control lanes
     * @return boolean whether there is a truck in waiting gate and place for truck in control lanes
     */
    protected boolean canTruckFromGateMove(int freeSlots){
        return (freeSlots >= 1 && !waitingLane.getGate().isEmpty());
    }

    /**
     *
     * @param freeSlots free slots in control lanes
     * @return boolean whether there is a place for both truck from waiting gate and first from lane
     */
    protected boolean canTruckFromLaneMove(int freeSlots){
        return (freeSlots == 2 && waitingLane.getTrucksAmount() > 0);
    }

    /**
     * responsible for moving truck from waiting gate to control lane
     */
    private void moveTruckFromGate(){
        Truck movingTruck = waitingLane.getGate().getProcessedTruck();
        selectQuickestAvailableControlLane().placeArrivingTruck(movingTruck);
        waitingLane.getGate().releaseTruck();
    }

    /**
     * responsible for moving truck from waiting lane if there is enough free spots
     * fot both waiting gate truck and first truck from waiting lane
     */
    private void moveFirstTruckFromLane(){
        Truck truckFromLane = waitingLane.getQueue().poll();
        selectQuickestAvailableControlLane().placeArrivingTruck(truckFromLane);
    }

    /**
     * method responsible for switching trucks between lanes
     * to achieve lower average processing time.
     * it is processed after invoking step() to prepare new turn
     */
    private void optimizeRoute(){
        TruckPlacement placement = countTruckPlacement();
        if(placement != TruckPlacement.balanced)
            reorganizeTrucks(placement);
    }

    /**
     * responsible for counting lane balance
     * @return truckPlacement describing trucks position
     */
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

    /**
     * responsible for switching trucks if optimization can be made
     * @param placement current placement of truck
     * @see CustomsControl#countTruckPlacement() to count current placement
     */
    private void reorganizeTrucks(TruckPlacement placement) {
        if(placement == TruckPlacement.firstLaneSided)
            moveLighterTrucksToLongerLane(secondLane, firstLane);
        else
            moveLighterTrucksToLongerLane(firstLane, secondLane);
    }

    /**
     * responsible for truck movement if optimization can be made
     * @param shorterLane lane with less trucks than longer lane
     * @param longerLane lane with more trucks than shorter lane
     */
    private void moveLighterTrucksToLongerLane(ControlLane shorterLane, ControlLane longerLane) {
        List<Truck> shorterLaneQueue = shorterLane.getQueue();
        List<Truck> longerLaneQueue = longerLane.getQueue();
        for(int truckIterator = immovableTrucks; truckIterator < shorterLane.getQueue().size(); truckIterator++){
            if(trucksShouldBeSwitched(shorterLaneQueue.get(truckIterator), longerLaneQueue.get(truckIterator))){
                switchTrucksBetweenLanes(shorterLaneQueue, longerLaneQueue, truckIterator);
            }
        }
    }

    /**
     *
     * @param shorterLaneTruck
     * @param longerLaneTruck
     * @return true if truck in shorter lane is lighter than the second one
     */
    private boolean trucksShouldBeSwitched(Truck shorterLaneTruck, Truck longerLaneTruck){
        return  shorterLaneTruck.isLighter(longerLaneTruck);
    }

    /**
     * switches trucks between lanes
     * @param shorterLaneQueue
     * @param longerLaneQueue
     * @param place
     */
    private void switchTrucksBetweenLanes(List<Truck> shorterLaneQueue, List<Truck> longerLaneQueue, int place) {
        Truck temporaryTruck = shorterLaneQueue.remove(place);
        shorterLaneQueue.add(place, longerLaneQueue.get(place));
        longerLaneQueue.remove(place);
        longerLaneQueue.add(place, temporaryTruck);
    }

    /**
     * method responsible for generation of new truck and placing it into simulation
     */
    public void generateTruck(){
        if(truckGenerator.isNewTruckGeneration()) {
            int truckWeight = truckGenerator.generateWeightForNewTruck();
            arrive(truckWeight);
        }
        else
            truckGenerator.incrementTruckGenerationCounter();
    }

    /**
     * turns off scheduled generation of new trucks (ON by default)
     */
    public void turnOffGeneration(){
        truckGenerator.turnOffGeneration();
    }

    /**
     * turns on scheduled generation of new trucks (ON by default)
     */
    public void turnOnGeneration(){
        truckGenerator.turnOnGeneration();
    }

    /**
     * changes params of scheduled generation of trucks
     * @param frequency to be set
     * @param weightCap to be set
     *                  for new generated trucks
     */
    public void changeGenerationParams(int frequency, int weightCap){
        turnOffGeneration();
        truckGenerator.setTruckGenerationFrequency(frequency);
        truckGenerator.setTruckWeightCapacity(weightCap);
        turnOnGeneration();
    }

    /**
     *
     * @param truckId identification of truck
     * @return estimated waiting time for truck to be processed
     */
    public int getWaitingTime(String truckId){
        if(isTruckInControlGates(truckId))
            return countTimeForControlGateTruck(truckId);
        else if(isTruckInControlLanes(truckId))
            return countTimeForControlLaneTruck(truckId);
        else if(isTruckInWaitingGate(truckId))
            return countTimeForWaitingGateTruck(truckId);
        else
            return countTimeForWaitingLaneTruck(truckId);
    }

    /**
     *
     * @param truckId
     * @return true if truck with given Id is currently in one of control gates
     */
    private boolean isTruckInControlGates(String truckId) {
        boolean isInFirstGate = false;
        boolean isInSecondGate = false;
        if(firstLane.getGate().getProcessedTruck() != null) {
            String firstGateTruckId = firstLane.getGate().getProcessedTruck().getTruckId();
            isInFirstGate = truckId.equals(firstGateTruckId);
        }
        if(secondLane.getGate().getProcessedTruck() != null) {
            String secondGateTruckId = secondLane.getGate().getProcessedTruck().getTruckId();
            isInSecondGate = truckId.equals(secondGateTruckId);
        }

        return (isInFirstGate || isInSecondGate);
    }

    /**
     * should be called only for trucks in control gates
     * @param truckId
     * @return time needed to be processed
     * @see CustomsControl#getWaitingTime(java.lang.String) otherwise
     */
    private int countTimeForControlGateTruck(String truckId) {
        Truck firstGateTruck = firstLane.getGate().getProcessedTruck();
        Truck secondGateTruck = secondLane.getGate().getProcessedTruck();
        if(truckId.equals(firstGateTruck.getTruckId())){
            ControlGate truckGate = firstLane.getGate();
            return firstGateTruck.getWeightAmount() - truckGate.getTimeInProcess();
        }
        else if(truckId.equals(secondGateTruck.getTruckId())) {
            ControlGate truckGate = secondLane.getGate();
            return secondGateTruck.getWeightAmount() - truckGate.getTimeInProcess();
        }
        else
            throw new TruckNotFoundException("Truck with requested ID " + truckId + " not found");
    }

    /**
     *
     * @param truckId
     * @return true if truck with given Id is currently in one of control lanes
     */
    private boolean isTruckInControlLanes(String truckId) {
        for(Truck truck : firstLane.getQueue()){
            if(truckId.equals(truck.getTruckId()))
                return true;
        }

        for(Truck truck : secondLane.getQueue()){
            if(truckId.equals(truck.getTruckId()))
                return true;
        }
        return false;
    }

    /**
     * should be called only for trucks in control lanes
     * @param truckId
     * @return time needed to be processed for truck in control lane
     * @see CustomsControl#getWaitingTime(java.lang.String) otherwise
     */
    private int countTimeForControlLaneTruck(String truckId) {
        ControlLane truckLane = null;
        for(Truck truck : firstLane.getQueue()){
            if(truckId.equals(truck.getTruckId())) {
                truckLane = firstLane;
                break;
            }
        }
        for(Truck truck : secondLane.getQueue()){
            if(truckId.equals(truck.getTruckId())) {
                truckLane = secondLane;
                break;
            }
        }
        if(truckLane == null)
            throw new TruckNotFoundException("Truck with requested ID " + truckId + " not found");
        int waitingTime = truckLane.getGate().getProcessedTruck().getWeightAmount() -
                truckLane.getGate().getTimeInProcess();
        for(Truck truck: truckLane.getQueue()){
            waitingTime += truck.getWeightAmount();
            if(truckId.equals(truck.getTruckId()))
                break;
        }
        return waitingTime;
    }

    /**
     *
     * @param truckId
     * @return true if truck with given Id is currently in waiting gate
     */
    private boolean isTruckInWaitingGate(String truckId) {
        Truck waitingGateTruck = waitingLane.getGate().getProcessedTruck();
        return truckId.equals(waitingGateTruck.getTruckId());
    }

    /**
     * should be called only for trucks in waiting gate
     * @param truckId
     * @return time needed to be processed for truck in waiting gate
     * @see CustomsControl#getWaitingTime(java.lang.String) otherwise
     */
    private int countTimeForWaitingGateTruck(String truckId) {
        Truck waitingGateTruck = waitingLane.getGate().getProcessedTruck();
        if(!truckId.equals(waitingGateTruck.getTruckId()))
            throw new TruckNotFoundException("Truck with requested ID " + truckId + " not found");
        int firstLaneTime = firstLane.getLaneProcessingTime();
        int secondLaneTime = secondLane.getLaneProcessingTime();
        int quickerTime = Math.min(firstLaneTime, secondLaneTime);
        return quickerTime + waitingGateTruck.getWeightAmount();
    }

    /**
     * should be called only for trucks in waiting lane
     * @param truckId
     * @return time needed to be processed for truck in waiting lane
     * @see CustomsControl#getWaitingTime(java.lang.String) otherwise
     */
    private int countTimeForWaitingLaneTruck(String truckId) {
        int firstLaneTime = firstLane.getLaneProcessingTime();
        int secondLaneTime = secondLane.getLaneProcessingTime();
        int waitingTime = (firstLaneTime + secondLaneTime)/2
                + waitingLane.getGate().getProcessedTruck().getWeightAmount();
        for(Truck truck: waitingLane.getQueue()){
            waitingTime += truck.getWeightAmount();
            if(truckId.equals(truck.getTruckId()))
                break;
        }
        return waitingTime;
    }

    /**
     * method responsible for displaying status of all trucks in simulation
     */
    public void status(){
        List<TruckStatus> statuses =  getAllTrucksStatuses();
        Collections.sort(statuses);
        printStatus(statuses);
    }

    /**
     * responsible for retrieving all trucks statuses
     * @return list of all trucks statuses in simulation
     */
    private List<TruckStatus> getAllTrucksStatuses() {
        List<TruckStatus> statuses = new ArrayList<>();
        getControlLaneStatuses(statuses, firstLane);
        getControlLaneStatuses(statuses, secondLane);
        getWaitingLaneStatuses(statuses, waitingLane);

        return statuses;
    }

    /**
     * responsible for retrieving statuses of trucks in given lane
     * @param statuses reference of list to place result in
     * @param lane to be checked
     */
    private void getControlLaneStatuses(List<TruckStatus> statuses, ControlLane lane) {
        Truck firstGateTruck = lane.getGate().getProcessedTruck();
        if(firstGateTruck != null) {
            statuses.add(new TruckStatus(firstGateTruck.getTruckId(),
                    countTimeForControlGateTruck(firstGateTruck.getTruckId()), TruckPosition.ControlGate));
        }
        for(Truck truck : lane.getTrucks()){
            statuses.add(new TruckStatus(truck.getTruckId(), countTimeForControlLaneTruck(truck.getTruckId()),
                    TruckPosition.ControlLane));
        }
    }

    /**
     * responsible for retrieving statuses of trucks in waiting lane
     * @param statuses reference of list to place result in
     * @param waitingLane
     */
    private void getWaitingLaneStatuses(List<TruckStatus> statuses, WaitingLane waitingLane) {
        Truck waitingGateTruck = waitingLane.getGate().getProcessedTruck();
        if(waitingGateTruck != null) {
            statuses.add(new TruckStatus(waitingGateTruck.getTruckId(),
                    countTimeForWaitingGateTruck(waitingGateTruck.getTruckId()), TruckPosition.WaitingGate));
        }
        for(Truck truck : waitingLane.getTrucks()){
            statuses.add(new TruckStatus(truck.getTruckId(), countTimeForWaitingLaneTruck(truck.getTruckId()),
                  TruckPosition.WaitingLane));
        }
    }

    /**
     * method displays to console all trucks in simulation
     * @param trucks all trucks in simulation
     */
    public void printStatus(List<TruckStatus> trucks){
        for(TruckStatus truckStatus: trucks){
            System.out.println(truckStatus.toString());
        }
    }

}
