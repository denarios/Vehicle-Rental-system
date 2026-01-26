package vehiclerentalsystem.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import vehiclerentalsystem.dto.StoreAnalyticsResponse;
import vehiclerentalsystem.enums.ReservationStatus;
import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.model.Reservation;
import vehiclerentalsystem.model.Store;
import vehiclerentalsystem.model.VehicleRentalSystem;

/**
 * Store analytics service for store owner dashboard.
 */
@Service
public class StoreAnalyticsService {

        private final VehicleRentalSystem system;

        public StoreAnalyticsService(VehicleRentalSystem system) {
                this.system = system;
        }

        /**
         * Get analytics for a specific store.
         * Used by store owners to view their dashboard.
         */
        public StoreAnalyticsResponse getStoreAnalytics(UUID storeId) {
                Store store = system.getStoreById(storeId);
                List<Reservation> storeReservations = store.getReservations();

                // Calculate metrics
                int totalVehicles = store.getInventory().getAllVehicle().values().stream()
                                .mapToInt(inv -> inv.getAllVehicles().size())
                                .sum();

                int totalReservations = storeReservations.size();

                int activeReservations = (int) storeReservations.stream()
                                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                                .count();

                int completedReservations = (int) storeReservations.stream()
                                .filter(r -> r.getStatus() == ReservationStatus.COMPLETED)
                                .count();

                double totalRevenue = storeReservations.stream()
                                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                                .mapToDouble(Reservation::getTotalPrice)
                                .sum();

                // Monthly revenue (last 30 days)
                LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
                double monthlyRevenue = storeReservations.stream()
                                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                                .filter(r -> r.getFromDate().isAfter(thirtyDaysAgo))
                                .mapToDouble(Reservation::getTotalPrice)
                                .sum();

                // Vehicle type breakdown
                Map<String, Integer> vehicleTypeBreakdown = new HashMap<>();
                for (VehicleType type : VehicleType.values()) {
                        int count = 0;
                        if (store.getInventory().getAllVehicle().containsKey(type)) {
                                count = store.getInventory().getAllVehicle().get(type).getAllVehicles().size();
                        }
                        vehicleTypeBreakdown.put(type.name(), count);
                }

                return new StoreAnalyticsResponse(
                                storeId,
                                store.getStoreName(),
                                store.getLocation().getCity(),
                                store.getLocation().getState(),
                                totalVehicles,
                                totalReservations,
                                activeReservations,
                                completedReservations,
                                totalRevenue,
                                monthlyRevenue,
                                vehicleTypeBreakdown);
        }
}
