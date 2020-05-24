package controlsystem;

public interface IGate {
    Truck getProcessedTruck();
    void processStep();
    void releaseTruck();
    void placeTruck(Truck truck);
    boolean isEmpty();
}
