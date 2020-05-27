package controlsystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class CustomsControlTest {
    Random random = new Random();
    CustomsControl customsControl;
    int weightCap = 10;
    int truckCounter;

    @Before
    public void setUp() throws Exception {
        customsControl = new CustomsControl();
        truckCounter = 0;

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void arrive() {
        int firstLaneCapacity = customsControl.getFirstLane().getLaneCapacity();
        int secondLaneCapacity = customsControl.getSecondLane().getLaneCapacity();
        int lanesCapacity = firstLaneCapacity + secondLaneCapacity;

        testControlLanesFilling(lanesCapacity);
        testWaitingGateFilling();
        testWaitingLanePlacing();
    }

    public void testControlLanesFilling(int lanesCapacity){
        testOneTruckArrival();
        controlLanesAreEmpty();
        testOneTruckArrival();
        controlGatesAreFull();

        /*lanes Capacity because we have 2 lanes and 2 gates - 2 trucks were added*/
        for(int truckNumber = 0; truckNumber < lanesCapacity; truckNumber++){
            testOneTruckArrival();
        }

        /*Now we have filled control lanes and gates*/
        controlLanesAreFull();
        controlGatesAreFull();
        waitingGateIsEmpty();
    }

    public void testWaitingGateFilling(){

        removeTruckFromFirstLane();
        testOneTruckArrival();

        removeTruckFromSecondLane();
        testOneTruckArrival();

        testOneTruckArrival();
        waitingGateIsFull();
    }

    public void testWaitingLanePlacing(){
        int waitingLaneTrucks = 0;

        testOneTruckArrival();
        waitingLaneTrucks++;
        truckPlacedWaitingLane(waitingLaneTrucks);

        testOneTruckArrival();
        waitingLaneTrucks++;
        truckPlacedWaitingLane(waitingLaneTrucks);
    }

    public void controlLanesAreEmpty(){
        int firstLaneTrucks = customsControl.getFirstLane().getTrucksAmount();
        int secondLaneTrucks = customsControl.getSecondLane().getTrucksAmount();
        int trucksInLanes = firstLaneTrucks + secondLaneTrucks;

        assertEquals(0, trucksInLanes);
    }

    public void controlLanesAreFull(){
        int firstLaneTrucks = customsControl.getFirstLane().getTrucksAmount();
        int secondLaneTrucks = customsControl.getSecondLane().getTrucksAmount();
        int trucksInLanes = firstLaneTrucks + secondLaneTrucks;
        int lanesCapacity = customsControl.getFirstLane().getLaneCapacity()
                + customsControl.getSecondLane().getLaneCapacity();

        assertEquals(lanesCapacity, trucksInLanes);
    }

    public void controlGatesAreFull(){
        boolean firstGateEmpty = customsControl.getFirstLane().getGate().isEmpty();
        boolean secondGateEmpty = customsControl.getSecondLane().getGate().isEmpty();

        assertFalse(firstGateEmpty);
        assertFalse(secondGateEmpty);
    }

    public void waitingGateIsEmpty(){
        boolean waitingGateEmpty = customsControl.getWaitingLane().getGate().isEmpty();
        assertTrue(waitingGateEmpty);
    }

    public void removeTruckFromFirstLane(){
        customsControl.getFirstLane().getQueue().pollLast();
    }

    public void removeTruckFromSecondLane(){
        customsControl.getSecondLane().getQueue().pollLast();
    }

    public void waitingGateIsFull(){
        boolean waitingGateEmpty = customsControl.getWaitingLane().getGate().isEmpty();
        assertFalse(waitingGateEmpty);
    }

    public void truckPlacedWaitingLane(int amount){
        int waitingLaneTrucks = customsControl.getWaitingLane().getTrucksAmount();
        assertEquals(amount, waitingLaneTrucks);
    }

    @Test
    public void testOneTruckArrival(){
        int truckWeight = random.nextInt(weightCap);
        customsControl.arrive(truckWeight);
        truckCounter++;
        assertEquals(truckCounter, customsControl.getTruckCounter());
    }

    @Test
    public void printStatus() {
        customsControl.printStatus();
    }

    @Test
    public void step() {
        customsControl.turnOffGeneration();
        customsControl.step();
        /*+2 so it cannot be 1 and does not disappear after step*/
        customsControl.arrive(random.nextInt(weightCap) + 2);
        customsControl.step();
        /*+2 for gates */
        int controlLanesCapacity = customsControl.getFirstLane().getLaneCapacity() +
                customsControl.getSecondLane().getLaneCapacity() + 2;

        while(customsControl.getTruckCounter() < controlLanesCapacity)
            customsControl.arrive(random.nextInt(weightCap) + 1);

        assertEquals(0, customsControl.countFreeSlots());
        assertNull(customsControl.getWaitingLane().getGate().getProcessedTruck());

        ControlGate firstLaneGate = customsControl.getFirstLane().getGate();
        /*Placing truck in waiting gate*/
        customsControl.arrive(random.nextInt(weightCap) + 1);

        assertNotNull(customsControl.getWaitingLane().getGate().getProcessedTruck());
        for(int stepIterator = firstLaneGate.getTimeInProcess();
            stepIterator < firstLaneGate.getProcessedTruck().getWeightAmount();stepIterator++){
            customsControl.step();
        }

        assertNull(customsControl.getWaitingLane().getGate().getProcessedTruck());
    }

    @Test
    public void canTruckFromGateMove() {
        Truck gateTruck = new Truck(random.nextInt(weightCap) + 1, "GateTruck");

        customsControl.getWaitingLane().getGate().placeTruck(gateTruck);
        assertFalse(customsControl.canTruckFromGateMove(0));
        assertTrue(customsControl.canTruckFromGateMove(1));
        assertTrue(customsControl.canTruckFromGateMove(10));

        customsControl.getWaitingLane().getGate().releaseTruck();
        assertFalse(customsControl.canTruckFromGateMove(0));
        assertFalse(customsControl.canTruckFromGateMove(1));
        assertFalse(customsControl.canTruckFromGateMove(10));

    }

    @Test
    public void canTruckFromLaneMove() {
        Truck laneTruck = new Truck(random.nextInt(weightCap) + 1, "laneTruck");

        /*First goes to Gate*/
        customsControl.getWaitingLane().placeArrivingTruck(laneTruck);
        customsControl.getWaitingLane().placeArrivingTruck(laneTruck);

        assertFalse(customsControl.canTruckFromLaneMove(0));
        assertFalse(customsControl.canTruckFromLaneMove(1));
        assertTrue(customsControl.canTruckFromLaneMove(2));
        assertFalse(customsControl.canTruckFromLaneMove(10));

        customsControl.getWaitingLane().getQueue().clear();

        assertFalse(customsControl.canTruckFromLaneMove(0));
        assertFalse(customsControl.canTruckFromLaneMove(1));
        assertFalse(customsControl.canTruckFromLaneMove(10));
    }

    @Test
    public void countFreeSlots() {
        /*+2 for gates*/
        int lanesCapacity = customsControl.getFirstLane().getLaneCapacity()
                + customsControl.getSecondLane().getLaneCapacity() + 2;


        assertEquals(lanesCapacity, customsControl.countFreeSlots());
        customsControl.arrive(random.nextInt(weightCap+1));
        customsControl.arrive(random.nextInt(weightCap+1));
        assertEquals(lanesCapacity - 2, customsControl.countFreeSlots());

        customsControl.arrive(random.nextInt(weightCap+1));
        customsControl.arrive(random.nextInt(weightCap+1));
        assertEquals(lanesCapacity - 4, customsControl.countFreeSlots());

        for(int fillIterator = 0; fillIterator < lanesCapacity - 4; fillIterator++){
            customsControl.arrive(random.nextInt(weightCap+1));
        }
        assertEquals(0, customsControl.countFreeSlots());

        customsControl.getFirstLane().getGate().releaseTruck();
        assertEquals(1, customsControl.countFreeSlots());

        customsControl.getSecondLane().getGate().releaseTruck();
        assertEquals(2, customsControl.countFreeSlots());

    }


    @Test
    public void getWaitingTime() {
        int weight = 10;
        String truckId = customsControl.arrive(weight);
        assertEquals(weight, customsControl.getWaitingTime(truckId));

        int secondWeight = 12;
        String secondTruckId = customsControl.arrive(secondWeight);
        assertEquals(secondWeight, customsControl.getWaitingTime(secondTruckId));

        int thirdWeight = 14;
        String thirdTruckId = customsControl.arrive(thirdWeight);
        assertEquals(thirdWeight + weight, customsControl.getWaitingTime(thirdTruckId));

        int fourthWeight = 16;
        String fourthTruckId = customsControl.arrive(fourthWeight);
        assertEquals(fourthWeight + secondWeight, customsControl.getWaitingTime(fourthTruckId));
    }

    @Test
    public void status() {
        customsControl.status();
    }

}