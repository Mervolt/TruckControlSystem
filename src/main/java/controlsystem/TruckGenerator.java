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

    /**
     *
     * @return current value of truck counter
     */
    public int getTruckCounter() {
        return truckCounter;
    }

    /**
     *
     * @return current value of truck generation counter
     */
    public int getTruckGenerationCounter() {
        return truckGenerationCounter;
    }

    /**
     *
     * @return current value of capacity
     */
    public int getTruckWeightCapacity() {
        return truckWeightCapacity;
    }

    /**
     *
     * @param truckWeightCapacity weight capacity to be set
     */
    public void setTruckWeightCapacity(int truckWeightCapacity) {
        this.truckWeightCapacity = truckWeightCapacity;
    }

    /**
     *
     * @return current value of frequency
     */
    public int getTruckGenerationFrequency(){
        return truckGenerationFrequency;
    }

    /**
     *
     * @param truckGenerationFrequency frequency to be set
     */
    public void setTruckGenerationFrequency(int truckGenerationFrequency) {
        this.truckGenerationFrequency = truckGenerationFrequency;
    }

    /**
     * turns on scheduled generation
     */
    public void turnOnGeneration(){
        generationEnabled = true;
    }

    /**
     * turns off scheduled generation
     * and restart counter
     */
    public void turnOffGeneration(){
        generationEnabled = false;
        truckGenerationCounter = truckGenerationCounterStarter;
    }


    /**
     *
     * @return boolean whether scheduled generation is turned on
     */
    public boolean isGenerationEnabled(){
        return generationEnabled;
    }

    public void incrementTruckGenerationCounter(){
        this.truckGenerationCounter++;
    }

    /**
     *
     * @return boolean if new truck should be generated
     */
    public boolean isNewTruckGeneration(){
        return truckGenerationCounter == truckGenerationFrequency;
    }

    /**
     *
     * @return weight for the truck to be generated
     */
    public int generateWeightForNewTruck(){
        truckGenerationCounter = truckGenerationCounterStarter;
        /* +1 to make it [1;capacity] */
        return random.nextInt(truckWeightCapacity) + 1;
    }

    /**
     *
     * @param truckWeight amount of weight for new truck
     * @return created new truck
     */
    public Truck createTruck(int truckWeight){
        return new Truck(truckWeight, generateTruckIdAndIncrementCounter());
    }

    /**
     * generated id for truck and increments truck in simulation counter
     * @return generated truck's id
     */
    public String generateTruckIdAndIncrementCounter() {
        String truckId = "T-" + this.truckCounter;
        this.truckCounter++;
        return truckId;
    }

}
