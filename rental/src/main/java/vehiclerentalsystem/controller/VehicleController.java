package vehiclerentalsystem.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.services.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/{storeId}")
    public Vehicle addVehicle(
            @PathVariable UUID storeId,
            @RequestBody AddVehicleRequest request) {
        return vehicleService.addVehicle(storeId, request.type(), request.status());
    }

    @GetMapping("/available/{storeId}")
    public Map<VehicleType, List<Vehicle>> getAvailableVehicles(
            @PathVariable UUID storeId) {
        return vehicleService.getAvailableVehicles(storeId);
    }

    @GetMapping("/{storeId}")
    public Map<VehicleType, List<Vehicle>> getAllVehicles(
            @PathVariable UUID storeId) {
        return vehicleService.getAllVehicles(storeId);
    }
}
