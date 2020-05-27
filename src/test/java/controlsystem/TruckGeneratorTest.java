package controlsystem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class TruckGeneratorTest {
    TruckGenerator truckGenerator;
    Random random;
    @Before
    public void setUp() throws Exception {
        truckGenerator = new TruckGenerator();
        random = new Random();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void turnOffGeneration() {
        truckGenerator.turnOnGeneration();
        truckGenerator.turnOffGeneration();
        boolean generationEnabled = truckGenerator.isGenerationEnabled();
        assertFalse(generationEnabled);
        assertEquals(truckGenerator.truckGenerationCounterStarter, truckGenerator.getTruckGenerationCounter());
    }

    @Test
    public void turnOnGeneration() {
        truckGenerator.turnOffGeneration();
        truckGenerator.turnOnGeneration();
        boolean generationEnabled = truckGenerator.isGenerationEnabled();
        assertTrue(generationEnabled);
        assertEquals(truckGenerator.truckGenerationCounterStarter, truckGenerator.getTruckGenerationCounter());
    }

    @Test
    public void incrementTruckGenerationCounter() {
        int counterBefore = truckGenerator.getTruckGenerationCounter();
        truckGenerator.incrementTruckGenerationCounter();
        assertNotEquals(counterBefore, truckGenerator.getTruckGenerationCounter());
        assertEquals(counterBefore + 1, truckGenerator.getTruckGenerationCounter());
    }

    @Test
    public void isNewTruckGeneration() {
        int counter = truckGenerator.getTruckGenerationCounter();
        int frequency = truckGenerator.getTruckGenerationFrequency();
        while(counter < frequency){
            assertFalse(truckGenerator.isNewTruckGeneration());
            truckGenerator.incrementTruckGenerationCounter();
            counter++;
        }
        assertTrue(truckGenerator.isNewTruckGeneration());
    }

    @Test
    public void generateWeightForNewTruck() {
        truckGenerator.incrementTruckGenerationCounter();
        int generatedWeight = truckGenerator.generateWeightForNewTruck();
        assertEquals(truckGenerator.truckGenerationCounterStarter, truckGenerator.getTruckGenerationCounter());
        assertTrue(generatedWeight <= truckGenerator.getTruckWeightCapacity());
        assertTrue(generatedWeight > 0);
    }

    @Test
    public void createTruck() {
        int truckWeight = random.nextInt(truckGenerator.getTruckWeightCapacity());
        Truck createdTruck = truckGenerator.createTruck(truckWeight);
        assertNotNull(createdTruck);
        assertNotNull(createdTruck.getTruckId());
        assertNotEquals(0, createdTruck.getWeightAmount());
    }

    @Test
    public void generateTruckIdAndIncrementCounter() {
        int truckCounter = truckGenerator.getTruckCounter();
        truckGenerator.generateTruckIdAndIncrementCounter();
        assertEquals(truckGenerator.getTruckGenerationCounter(), truckCounter + 1);
        int numberInNextId = truckGenerator.getTruckCounter();
        String expectedId = "T-" + numberInNextId;
        assertEquals(expectedId, truckGenerator.generateTruckIdAndIncrementCounter());
    }
}