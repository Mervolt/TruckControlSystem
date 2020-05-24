package controlsystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ControlLaneTest {
    Random random = new Random();
    ControlLane controlLane;
    int weightCap = 10;
    int testTruckCounter = 0;

    @Before
    public void setUp() throws Exception {
        controlLane = new ControlLane();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void hasFreePlace() {
        placeOneTruck();
        assertTrue(controlLane.hasFreePlace());

        /*now fill lane*/
        placeManyTrucks(controlLane.getLaneCapacity());

        assertFalse(controlLane.hasFreePlace());
    }

    public void placeOneTruck(){
        Truck truck = createTruck(random.nextInt(weightCap));
        controlLane.placeArrivingTruck(truck);
    }

    public void placeManyTrucks(int trucksAmount){
        for(int i = 0; i < trucksAmount; i++)
            controlLane.placeArrivingTruck(createTruck(random.nextInt(weightCap)));
    }

    public Truck createTruck(int weight){
        testTruckCounter++;
        return new Truck(weight, "Gate-Test-" + testTruckCounter);
    }

    @Test
    public void getLaneProcessingTime() {
        int weight = random.nextInt(weightCap);
        /*It is placed in gate*/
        placeOneTruck(weight);
        assertEquals(weight, controlLane.getLaneProcessingTime());

        int previousWeight = weight;
        weight = random.nextInt(weightCap);
        placeOneTruck(weight);
        assertEquals(weight + previousWeight, controlLane.getLaneProcessingTime());
    }

    public void placeOneTruck(int weight){
        Truck truck = createTruck(weight);
        controlLane.placeArrivingTruck(truck);
    }


    @Test
    public void placeArrivingTruck() {
        placeOneTruck();
        assertEquals(0, controlLane.getTrucksAmount());
        placeOneTruck();
        /*because 1 in gate*/
        assertEquals(1, controlLane.getTrucksAmount());
        placeOneTruck();
        assertEquals(2, controlLane.getTrucksAmount());
    }

    @Test
    public void getTrucksAmount() {
        placeOneTruck();
        /*placed in gate so in queue 0!*/
        assertEquals(0, controlLane.getTrucksAmount());

        placeOneTruck();
        assertEquals(1, controlLane.getTrucksAmount());

        placeManyTrucks(controlLane.getLaneCapacity() - 1);
        assertEquals(controlLane.getLaneCapacity(), controlLane.getTrucksAmount());
    }
}