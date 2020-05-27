package controlsystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TruckTest {
    Truck smallTruck = new Truck(3, "Small");
    Truck mediumTruck = new Truck(15, "Medium");
    Truck bigTruck = new Truck(50, "Big");

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isLighter() {
        assertTrue(smallTruck.isLighter(mediumTruck));
        assertTrue(smallTruck.isLighter(bigTruck));
        assertTrue(mediumTruck.isLighter(bigTruck));

        assertFalse(mediumTruck.isLighter(smallTruck));
        assertFalse(bigTruck.isLighter(smallTruck));
        assertFalse(bigTruck.isLighter(mediumTruck));
    }
}