package vehiclerentalsystem.model;

import java.util.UUID;

import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;

public class Vehicle {
    private UUID ID;
    private VehicleType type;
    private VehicleStatus status;

    public Vehicle(VehicleType type,VehicleStatus status){
        this.type = type;
        this.status = status;
        this.ID = UUID.randomUUID();
    }

    public UUID getId(){
        return ID;
    }

    public VehicleType getType(){
        return type;
    }

    public VehicleStatus getStatus(){
        return status;
    }
    public void setType(VehicleType type){
        this.type = type;
    }
    public void setStatus(VehicleStatus status){
        this.status = status;
    }
    public boolean isAvailable() {
        return status == VehicleStatus.ACTIVE;
    }

}
