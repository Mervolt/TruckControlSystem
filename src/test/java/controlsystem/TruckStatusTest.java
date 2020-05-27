package controlsystem;

import org.junit.Test;

import static org.junit.Assert.*;

public class TruckStatusTest {

    @Test
    public void compareTo() {
        TruckStatus shortTime = new TruckStatus("Short", 1, TruckPosition.ControlGate);
        TruckStatus mediumTime = new TruckStatus("Medium", 15, TruckPosition.ControlLane);
        TruckStatus longTime = new TruckStatus("Long", 50, TruckPosition.WaitingGate);
        assertTrue(longTime.compareTo(mediumTime) > 0);
        assertTrue(longTime.compareTo(shortTime) > 0);
        assertTrue(mediumTime.compareTo(shortTime) > 0);
        assertTrue(mediumTime.compareTo(longTime) < 0);
        assertTrue(shortTime.compareTo(longTime) < 0);
        assertTrue(shortTime.compareTo(mediumTime) < 0);

    }
}