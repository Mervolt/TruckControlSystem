package controlsystem;

import java.util.List;

public interface ILane {
    void placeArrivingTruck(Truck truck);
    int getTrucksAmount();
    List<Truck> getTrucks();
    IGate getGate();
    void processStep();
}
