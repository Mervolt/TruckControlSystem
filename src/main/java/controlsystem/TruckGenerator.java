package controlsystem;

import java.util.Random;

public class TruckGenerator{
    /*10 by default*/
    private int truckWeightCapacity = 10;
    /*Each 5 turns by default*/
    private int truckGenerationFrequency = 5;
    private int truckGenerationCounter = 1;
    private int truckCounter;
    private boolean generationEnabled = true;
    private Random random = new Random();
    protected int truckGenerationCounterStarter = 1;

    public void turnOffGeneration(){
        generationEnabled = false;
        truckGenerationCounter = truckGenerationCounterStarter;
    }

    public int getTruckCounter() {
        return truckCounter;
    }

    public int getTruckGenerationCounter() {
        return truckGenerationCounter;
    }

    public int getTruckWeightCapacity() {
        return truckWeightCapacity;
    }

    public void setTruckWeightCapacity(int truckWeightCapacity) {
        this.truckWeightCapacity = truckWeightCapacity;
    }

    public int getTruckGenerationFrequency(){
        return truckGenerationFrequency;
    }

    public void setTruckGenerationFrequency(int truckGenerationFrequency) {
        this.truckGenerationFrequency = truckGenerationFrequency;
    }

    public void turnOnGeneration(){
        generationEnabled = true;
    }

    public boolean isGenerationEnabled(){
        return generationEnabled;
    }

    public void incrementTruckGenerationCounter(){
        this.truckGenerationCounter++;
    }

    public boolean isNewTruckGeneration(){
        return truckGenerationCounter == truckGenerationFrequency;
    }

    public int generateWeightForNewTruck(){
        truckGenerationCounter = truckGenerationCounterStarter;
        /* +1 to make it [1;capacity] */
        return random.nextInt(truckWeightCapacity) + 1;
    }

    public Truck createTruck(int truckWeight){
        return new Truck(truckWeight, generateTruckIdAndIncrementCounter());
    }

    public String generateTruckIdAndIncrementCounter() {
        String truckId = "T-" + this.truckCounter;
        this.truckCounter++;
        return truckId;
    }

}
