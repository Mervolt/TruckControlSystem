package controlsystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ControlLane implements ILane {
    private LinkedList<Truck> queue;
    private ControlGate gate;
    private int laneCapacity = 5;

    /**
     * default constructor
     */
    public ControlLane() {
        this.gate = new ControlGate();
        this.queue = new LinkedList<>();
    }

    /**
     *
     * @return reference to trucks queue
     */
    public LinkedList<Truck> getQueue() {
        return queue;
    }

    /**
     *
     * @return reference to control gate
     */
    public ControlGate getGate() {
        return gate;
    }

    /**
     *
     * @return current lane capacity
     */
    public int getLaneCapacity() {
        return laneCapacity;
    }

    /**
     *
     * @return true if there is free place in lane
     */
    public boolean hasFreePlace() {
        return laneCapacity != getLaneTruckAmount();
    }

    /**
     *
     * @return amount of trucks in lane
     */
    private int getLaneTruckAmount(){
        return queue.size();
    }

    /**
     *
     * @return time needed to process truck in gate and all trucks currently placed in lane
     */
    public int getLaneProcessingTime(){
        int totalTime = 0;
        for(Truck truck : queue)
            totalTime += truck.getWeightAmount();
        totalTime += gate.getProcessedTruck().getWeightAmount() - gate.getTimeInProcess();
        return totalTime;
    }

    /**
     * places truck in lane
     * @param arrivingTruck truck to be placed in lane, and if possible in gate
     */
    @Override
    public void placeArrivingTruck(Truck arrivingTruck){
        if(gate.getProcessedTruck() == null)
            gate.placeTruck(arrivingTruck);
        else
            queue.add(arrivingTruck);
    }

    /**
     * method is responsible for processing step for control lane
     * i.e. it moves trucks along lane, and if possible places fist in gate
     */
    @Override
    public void processStep() {
        gate.processStep();
        if(gate.isEmpty())
            moveTruckToGate();
    }

    /**
     * method responsible for moving first truck from lane to gate
     */
    private void moveTruckToGate() {
        Truck firstTruck = queue.poll();
        gate.placeTruck(firstTruck);
    }

    /**
     *
     * @return amount of trucks currently in lane
     */
    @Override
    public int getTrucksAmount(){
        return queue.size();
    }

    /**
     *
     * @return copy of trucks list currently in lane
     */
    @Override
    public List<Truck> getTrucks() {
        return new ArrayList<>(queue);
    }
}
