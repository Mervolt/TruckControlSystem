package controlsystem;

public interface IGate {
    int getTimeInProcess();
    Truck getProcessedTruck();
    void releaseTruck();
    void placeTruck(Truck truck);
    boolean isEmpty();
}
