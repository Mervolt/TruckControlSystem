package controlsystem;

import java.util.List;

public interface ILane {
    void placeArrivingTruck(Truck truck);
    void processStep();
    int getTrucksAmount();
    List<Truck> getTrucks();
    IGate getGate();
}
