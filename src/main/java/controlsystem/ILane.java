package controlsystem;

public interface ILane {
    void placeArrivingTruck(Truck truck);
    void processStep();
    int getTrucksAmount();
}
