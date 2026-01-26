package vehiclerentalsystem.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.services.VehicleService;

/**
 * REST Controller for vehicle management.
 * 
 * Provides endpoints for:
 * - Adding vehicles (Factory Pattern)
 * - Searching vehicles (Specification Pattern)
 * - Listing available/all vehicles
 */
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final vehiclerentalsystem.services.VehicleSearchService vehicleSearchService;

    public VehicleController(VehicleService vehicleService,
            vehiclerentalsystem.services.VehicleSearchService vehicleSearchService) {
        this.vehicleService = vehicleService;
        this.vehicleSearchService = vehicleSearchService;
    }

    /**
     * Search vehicles by location (city and/or state).
     * 
     * GET /api/vehicles/search?city=Hyderabad&state=Telangana
     */
    @GetMapping("/search")
    public List<Vehicle> searchVehicles(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state) {
        return vehicleSearchService.searchByLocation(city, state);
    }

    /**
     * Get all vehicles across all stores.
     * 
     * GET /api/vehicles
     */
    @GetMapping
    public List<Vehicle> getAllVehiclesAcrossStores() {
        return vehicleSearchService.searchByLocation(null, null);
    }

    /**
     * Add a new vehicle to a store.
     * Uses Factory Pattern to create the appropriate vehicle type.
     */
    @PostMapping("/{storeId}")
    public Vehicle addVehicle(
            @PathVariable UUID storeId,
            @RequestBody AddVehicleRequest request) {
        return vehicleService.addVehicle(storeId, request.type(), request.status());
    }

    /**
     * Get available vehicles at a store.
     */
    @GetMapping("/available/{storeId}")
    public Map<VehicleType, List<Vehicle>> getAvailableVehicles(
            @PathVariable UUID storeId) {
        return vehicleService.getAvailableVehicles(storeId);
    }

    /**
     * Get all vehicles at a store.
     */
    @GetMapping("/{storeId}")
    public Map<VehicleType, List<Vehicle>> getAllVehicles(
            @PathVariable UUID storeId) {
        return vehicleService.getAllVehicles(storeId);
    }

    /**
     * Search vehicles using Specification Pattern.
     * 
     * Supports multi-criteria search with dynamic filtering:
     * - type: Filter by vehicle type (CAR, BIKE, TRUCK)
     * - status: Filter by status (ACTIVE, INACTIVE)
     * - minPrice: Minimum daily rate
     * - maxPrice: Maximum daily rate
     * 
     * Example: GET
     * /api/vehicles/search/{storeId}?type=CAR&status=ACTIVE&minPrice=30&maxPrice=100
     */
    @GetMapping("/search/{storeId}")
    public List<Vehicle> searchVehicles(
            @PathVariable UUID storeId,
            @RequestParam(required = false) VehicleType type,
            @RequestParam(required = false) VehicleStatus status,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        return vehicleService.searchVehicles(storeId, type, status, minPrice, maxPrice);
    }
}
