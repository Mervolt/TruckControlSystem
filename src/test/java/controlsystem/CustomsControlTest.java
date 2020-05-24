package controlsystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
        System.out.println(customsControl);
    }

    @Test
    public void step() {
        System.out.println(customsControl);
    }
}