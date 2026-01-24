package vehiclerentalsystem.controller;

import java.time.LocalDate;
import java.util.UUID;

public record ReservationRequest(
        UUID storeId,
        UUID userId,
        UUID vehicleId,
        LocalDate fromDate,
        LocalDate toDate
) {}
