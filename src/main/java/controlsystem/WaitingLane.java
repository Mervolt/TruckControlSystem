package controlsystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WaitingLane implements ILane {
    private LinkedList<Truck> queue;
    private IGate gate;

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

    @Override
    public void placeArrivingTruck(Truck arrivingTruck) {
        if(gate.getProcessedTruck() == null)
            gate.placeTruck(arrivingTruck);
        else
            this.queue.add(arrivingTruck);
    }


    @Override
    public void processStep() {
       Truck movingTruck = gate.getProcessedTruck();
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
