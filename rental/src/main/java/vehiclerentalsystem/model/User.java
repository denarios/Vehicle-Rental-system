package vehiclerentalsystem.model;

import java.util.UUID;

public class User {

    private  UUID id;
    private  String name;
    private  boolean drivingLicence;


    public User(String name, boolean drivingLicence) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.drivingLicence = drivingLicence;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean hasDrivingLicence() {
        return drivingLicence;
    }
}
