package controlsystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WaitingLane implements ILane {
    private LinkedList<Truck> queue;
    private WaitingGate gate;

    public WaitingLane() {
        this.queue = new LinkedList<>();
        this.gate = new WaitingGate();
    }

    public LinkedList<Truck> getQueue() {
        return queue;
    }

    @Override
    public IGate getGate() {
        return gate;
    }

    /**
     *
     * @param arrivingTruck to be placed in lane
     */
    @Override
    public void placeArrivingTruck(Truck arrivingTruck) {
        if(gate.getProcessedTruck() == null)
            gate.placeTruck(arrivingTruck);
        else
            this.queue.add(arrivingTruck);
    }

    /**
     * responsible for processing one time unit step in simulation to trucks in lane
     */
    @Override
    public void processStep() {
        if(gate.isEmpty())
            moveTruckToGate();
        gate.incrementTimeInProcess();
    }

    /**
     * moves first truck from lane to gate
     */
    private void moveTruckToGate() {
        Truck firstTruck = queue.poll();
        gate.placeTruck(firstTruck);
    }

    @Override
    public int getTrucksAmount(){
        return queue.size();
    }

    @Override
    public List<Truck> getTrucks() {
        return new ArrayList<>(queue);
    }

}
