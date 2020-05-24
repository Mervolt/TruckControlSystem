package controlsystem;

import java.util.LinkedList;
public class ControlLane implements ILane {
    private LinkedList<Truck> queue;
    private ControlGate gate;
    private int laneCapacity = 5;

    public ControlLane() {
        this.gate = new ControlGate();
        this.queue = new LinkedList<>();
    }

    public LinkedList<Truck> getQueue() {
        return queue;
    }

    public IGate getGate() {
        return gate;
    }

    public int getLaneCapacity() {
        return laneCapacity;
    }

    public boolean hasFreePlace() {
        return laneCapacity != getLaneTruckAmount();
    }

    private int getLaneTruckAmount(){
        return queue.size();
    }

    public int getLaneProcessingTime(){
        int totalTime = 0;
        for(Truck truck : queue)
            totalTime += truck.getWeightAmount();
        totalTime += gate.getProcessedTruck().getWeightAmount() - gate.getTimeInProcess();
        return totalTime;
    }

    @Override
    public void placeArrivingTruck(Truck arrivingTruck){
        if(gate.getProcessedTruck() == null)
            gate.placeTruck(arrivingTruck);
        else
            queue.add(arrivingTruck);
    }

    @Override
    public void processStep() {
        gate.processStep();
        if(gate.isEmpty())
            moveTruckToGate();
    }

    private void moveTruckToGate() {
        Truck firstTruck = queue.poll();
        gate.placeTruck(firstTruck);
    }

    @Override
    public int getTrucksAmount(){
        return queue.size();
    }
}
