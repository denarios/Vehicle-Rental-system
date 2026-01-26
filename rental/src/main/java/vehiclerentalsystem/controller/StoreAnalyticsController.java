package vehiclerentalsystem.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vehiclerentalsystem.dto.StoreAnalyticsResponse;
import vehiclerentalsystem.services.StoreAnalyticsService;

/**
 * Store analytics controller.
 * Provides analytics data for store owner dashboards.
 */
@RestController
@RequestMapping("/api/stores")
public class StoreAnalyticsController {

    private final StoreAnalyticsService analyticsService;

    public StoreAnalyticsController(StoreAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * Get analytics for a specific store.
     * 
     * GET /api/stores/{storeId}/analytics
     * 
     * Used by store owners to view their dashboard metrics.
     */
    @GetMapping("/{storeId}/analytics")
    public ResponseEntity<StoreAnalyticsResponse> getStoreAnalytics(@PathVariable UUID storeId) {
        StoreAnalyticsResponse analytics = analyticsService.getStoreAnalytics(storeId);
        return ResponseEntity.ok(analytics);
    }
}
