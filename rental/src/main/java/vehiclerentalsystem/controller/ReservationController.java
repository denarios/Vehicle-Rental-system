package vehiclerentalsystem.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import vehiclerentalsystem.model.Reservation;
import vehiclerentalsystem.services.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public Reservation reserve(@RequestBody ReservationRequest request) {
        return reservationService.createReservation(
                request.storeId(),
                request.userId(),
                request.vehicleId(),
                request.fromDate(),
                request.toDate());
    }

    @GetMapping("/store/{storeId}")
    public List<Reservation> getReservationsByStore(@PathVariable UUID storeId) {
        return reservationService.getReservationsByStore(storeId);
    }

    @GetMapping("/user/{userId}")
    public List<Reservation> getReservationsByUser(@PathVariable UUID userId) {
        return reservationService.getReservationsByUser(userId);
    }

    @PutMapping("/{storeId}/{reservationId}/cancel")
    public Reservation cancelReservation(
            @PathVariable UUID storeId,
            @PathVariable UUID reservationId) {
        return reservationService.cancelReservation(storeId, reservationId);
    }

    @PutMapping("/{storeId}/{reservationId}/complete")
    public Reservation completeReservation(
            @PathVariable UUID storeId,
            @PathVariable UUID reservationId) {
        return reservationService.completeReservation(storeId, reservationId);
    }
}
