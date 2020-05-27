package controlsystem;

public class TruckNotFoundException extends RuntimeException {
    public TruckNotFoundException(String message) {
        super(message);
    }
}
