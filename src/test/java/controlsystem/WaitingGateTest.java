package controlsystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class WaitingGateTest {
    Random random = new Random();
    WaitingGate waitingGate;
    int weightCap = 10;
    int testTruckCounter = 0;

    @Before
    public void setUp() throws Exception {
        waitingGate = new WaitingGate();
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
        waitingGate.placeTruck(truck);
        assertEquals(truck, waitingGate.getProcessedTruck());
    }

    @Test
    public void releaseTruck() {
        placeAndGetTruck(random.nextInt(weightCap));
        assertNotNull(waitingGate.getProcessedTruck());
        waitingGate.releaseTruck();
        assertNull(waitingGate.getProcessedTruck());
    }

    @Test
    public void placeTruck() {
        placeAndGetTruck(random.nextInt(weightCap));
        placeAndGetTruck(random.nextInt(weightCap));
        placeAndGetTruck(0);
        placeNullTruck();
    }

    public void placeNullTruck(){
        waitingGate.placeTruck(null);
        assertNull(waitingGate.getProcessedTruck());
    }

    @Test
    public void isEmpty() {
        assertTrue(waitingGate.isEmpty());
        placeAndGetTruck(random.nextInt(weightCap));
        assertFalse(waitingGate.isEmpty());
        waitingGate.releaseTruck();
        assertTrue(waitingGate.isEmpty());
    }
}