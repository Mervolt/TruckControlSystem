package controlsystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ControlGateTest {
    Random random = new Random();
    ControlGate controlGate;
    int weightCap = 10;
    int testTruckCounter = 0;

    @Before
    public void setUp() throws Exception {
        controlGate = new ControlGate();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getProcessedTruck() {
        placeAndGetTruck(random.nextInt(weightCap));
    }

    public Truck createTruck(int weight){
        testTruckCounter++;
        return new Truck(weight, "Gate-Test-" + testTruckCounter);
    }

    public void placeAndGetTruck(int weight){
        Truck truck = createTruck(weight);
        controlGate.placeTruck(truck);
        assertEquals(truck, controlGate.getProcessedTruck());
    }

    @Test
    public void placeTruck() {
        placeAndGetTruck(random.nextInt(weightCap));
        placeAndGetTruck(random.nextInt(weightCap));
        placeAndGetTruck(0);
        placeNullTruck();
    }

    public void placeNullTruck(){
        controlGate.placeTruck(null);
        assertNull(controlGate.getProcessedTruck());
    }

    @Test
    public void isEmpty() {
        assertTrue(controlGate.isEmpty());
        placeAndGetTruck(random.nextInt(weightCap));
        assertFalse(controlGate.isEmpty());
        controlGate.releaseTruck();
        assertTrue(controlGate.isEmpty());
    }

}