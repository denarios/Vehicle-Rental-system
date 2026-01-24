package vehiclerentalsystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import vehiclerentalsystem.model.Store;
import vehiclerentalsystem.services.StoreService;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    public Store addStore(@RequestBody CreateStoreRequest request) {
        return storeService.createStore(
                request.state(),
                request.district(),
                request.pincode());
    }

    @GetMapping
    public List<Store> getStores() {
        return storeService.getAllStores();
    }
}