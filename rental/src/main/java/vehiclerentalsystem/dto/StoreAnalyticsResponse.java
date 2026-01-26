package vehiclerentalsystem.dto;

import java.util.Map;
import java.util.UUID;

/**
 * Store analytics response DTO.
 * 
 * Contains statistics for store owner dashboard.
 */
public record StoreAnalyticsResponse(
        UUID storeId,
        String storeName,
        String city,
        String state,
        int totalVehicles,
        int totalReservations,
        int activeReservations,
        int completedReservations,
        double totalRevenue,
        double monthlyRevenue,
        Map<String, Integer> vehicleTypeBreakdown // CAR: 10, BIKE: 5, etc.
) {
}
