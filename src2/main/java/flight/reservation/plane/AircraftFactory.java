package flight.reservation.plane;

public final class AircraftFactory {
    private AircraftFactory() {
    }

    public static Aircraft create(String model) {
        switch (model) {
            case "A380":
            case "A350":
            case "Embraer 190":
            case "Antonov AN2":
                return new PassengerPlane(model);
            case "H1":
            case "H2":
                return new Helicopter(model);
            case "HypaHype":
                return new PassengerDrone(model);
            default:
                throw new IllegalArgumentException(String.format("Model type '%s' is not recognized", model));
        }
    }
}
