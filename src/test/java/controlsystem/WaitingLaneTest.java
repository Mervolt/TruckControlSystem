package controlsystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class WaitingLaneTest {
    Random random = new Random();
    WaitingLane waitingLane;
    int weightCap = 10;
    int testTruckCounter = 0;

    @Before
    public void setUp() throws Exception {
        waitingLane = new WaitingLane();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void placeArrivingTruck() {
        placeOneTruck();
        assertEquals(0, waitingLane.getTrucksAmount());
        placeOneTruck();
        /*because 1 in gate*/
        assertEquals(1, waitingLane.getTrucksAmount());
        placeOneTruck();
        assertEquals(2, waitingLane.getTrucksAmount());
    }

    @Test
    public void getTrucksAmount() {
        placeOneTruck();
        /*placed in gate so in queue 0!*/
        assertEquals(0, waitingLane.getTrucksAmount());

        placeOneTruck();
        assertEquals(1, waitingLane.getTrucksAmount());

        int generatedNumber = random.nextInt(100);
        placeManyTrucks(generatedNumber - 1);
        assertEquals(generatedNumber, waitingLane.getTrucksAmount());
    }

    public void placeOneTruck(){
        Truck truck = createTruck(random.nextInt(weightCap));
        waitingLane.placeArrivingTruck(truck);
    }

    public void placeManyTrucks(int trucksAmount){
        for(int i = 0; i < trucksAmount; i++)
            waitingLane.placeArrivingTruck(createTruck(random.nextInt(weightCap)));
    }

    public Truck createTruck(int weight){
        testTruckCounter++;
        return new Truck(weight, "Gate-Test-" + testTruckCounter);
    }
}