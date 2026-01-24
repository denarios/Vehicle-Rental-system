package vehiclerentalsystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import vehiclerentalsystem.model.User;
import vehiclerentalsystem.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request.name(), request.drivingLicence());
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }
}
