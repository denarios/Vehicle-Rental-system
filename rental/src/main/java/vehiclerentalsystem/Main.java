package vehiclerentalsystem;

import java.time.LocalDate;

import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.model.*;

public class Main {

    public static void main(String[] args) {

        VehicleRentalSystem system = new VehicleRentalSystem();

        /* -------- Users -------- */
        User user1 = new User("Rahul", true);
        User user2 = new User("Amit", true);

        system.addUser(user1);
        system.addUser(user2);

        /* -------- Store -------- */
        Store bangaloreStore =
                new Store("Karnataka", "Bangalore", "560001");

        system.addStore(bangaloreStore);

        /* -------- Vehicles -------- */
        Vehicle bike1 = new Vehicle(VehicleType.BIKE, VehicleStatus.ACTIVE);
        Vehicle bike2 = new Vehicle(VehicleType.BIKE, VehicleStatus.ACTIVE);
        Vehicle bike3 = new Vehicle(VehicleType.BIKE, VehicleStatus.ACTIVE);
        Vehicle car1  = new Vehicle(VehicleType.CAR, VehicleStatus.ACTIVE);

        bangaloreStore.addVehicle(bike1);
        bangaloreStore.addVehicle(bike2);
        bangaloreStore.addVehicle(bike3);
        bangaloreStore.addVehicle(car1);

        /* -------- Before Booking -------- */
        System.out.println("=== Before Booking ===");
        system.showAvailableVehiclesAcrossStores();

        /* -------- Reservation -------- */
        bangaloreStore.reserveVehicle(
                user1,
                bike1,
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );

        /* -------- After Booking -------- */
        System.out.println("\n=== After Booking ===");
        system.showAvailableVehiclesAcrossStores();
    }
}
