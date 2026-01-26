package vehiclerentalsystem.config;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import vehiclerentalsystem.enums.UserRole;
import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.model.Store;
import vehiclerentalsystem.model.User;
import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.model.VehicleRentalSystem;
import vehiclerentalsystem.factory.VehicleFactory;

/**
 * Initialize demo data with Indian cities.
 */
@Configuration
public class DemoDataConfig {

    private final VehicleRentalSystem system;

    public DemoDataConfig(VehicleRentalSystem system) {
        this.system = system;
    }

    @PostConstruct
    public void initDemoData() {
        System.out.println("=".repeat(60));
        System.out.println("🏙️  Initializing Demo Data (Indian Cities)");
        System.out.println("=".repeat(60));

        // Create stores in major Indian cities
        Store hyderabadStore = new Store("Telangana", "Hyderabad", "Hyderabad", "500001");
        Store bangaloreStore = new Store("Karnataka", "Bangalore", "Bangalore", "560001");
        Store mumbaiStore = new Store("Maharashtra", "Mumbai", "Mumbai", "400001");

        // Add vehicles to Hyderabad store
        hyderabadStore.addVehicle(VehicleFactory.createVehicle(VehicleType.CAR, VehicleStatus.ACTIVE));
        hyderabadStore.addVehicle(VehicleFactory.createVehicle(VehicleType.CAR, VehicleStatus.ACTIVE));
        hyderabadStore.addVehicle(VehicleFactory.createVehicle(VehicleType.BIKE, VehicleStatus.ACTIVE));
        hyderabadStore.addVehicle(VehicleFactory.createVehicle(VehicleType.TRUCK, VehicleStatus.ACTIVE));

        // Add vehicles to Bangalore store
        bangaloreStore.addVehicle(VehicleFactory.createVehicle(VehicleType.CAR, VehicleStatus.ACTIVE));
        bangaloreStore.addVehicle(VehicleFactory.createVehicle(VehicleType.BIKE, VehicleStatus.ACTIVE));
        bangaloreStore.addVehicle(VehicleFactory.createVehicle(VehicleType.BIKE, VehicleStatus.ACTIVE));

        // Add vehicles to Mumbai store
        mumbaiStore.addVehicle(VehicleFactory.createVehicle(VehicleType.CAR, VehicleStatus.ACTIVE));
        mumbaiStore.addVehicle(VehicleFactory.createVehicle(VehicleType.TRUCK, VehicleStatus.ACTIVE));

        // Add stores to system
        system.addStore(hyderabadStore);
        system.addStore(bangaloreStore);
        system.addStore(mumbaiStore);

        // Create store owner for Hyderabad
        User storeOwner = new User(
                "Raj Kumar",
                "raj.hyderabad@rental.com",
                "store123",
                true,
                UserRole.STORE_OWNER);
        storeOwner.setAssignedStoreId(hyderabadStore.getStoreId());
        system.addUser(storeOwner);

        System.out.println("✅ Created 3 stores:");
        System.out.println("   🏙️  Hyderabad, Telangana (4 vehicles)");
        System.out.println("   🏙️  Bangalore, Karnataka (3 vehicles)");
        System.out.println("   🏙️  Mumbai, Maharashtra (2 vehicles)");
        System.out.println();
        System.out.println("✅ Created store owner:");
        System.out.println("   Store Owner: raj.hyderabad@rental.com / store123");
        System.out.println("=".repeat(60));
    }
}
