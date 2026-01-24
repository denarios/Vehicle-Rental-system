package vehiclerentalsystem.controller;

public record CreateStoreRequest(
        String state,
        String district,
        String pincode
        //(request.state(), request.district(),request.pincode());
) {}
