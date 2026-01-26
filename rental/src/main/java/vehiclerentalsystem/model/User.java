package vehiclerentalsystem.model;

import java.util.UUID;

import vehiclerentalsystem.enums.UserRole;

/**
 * User model with role-based access control.
 * 
 * Enhanced with:
 * - Email for login
 * - Password for authentication
 * - Role for authorization (CUSTOMER, STORE_MANAGER, ADMIN)
 * - Assigned store ID for store managers
 */
public class User {

    private UUID id;
    private String name;
    private String email;
    private String password; // In production: hash this with BCrypt!
    private boolean drivingLicence;
    private UserRole role;
    private UUID assignedStoreId; // For STORE_MANAGER role

    // Constructor for customers
    public User(String name, String email, String password, boolean drivingLicence) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
        this.drivingLicence = drivingLicence;
        this.role = UserRole.CUSTOMER; // Default role
        this.assignedStoreId = null;
    }

    // Constructor with role
    public User(String name, String email, String password, boolean drivingLicence, UserRole role) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
        this.drivingLicence = drivingLicence;
        this.role = role;
        this.assignedStoreId = null;
    }

    // Legacy constructor for backward compatibility
    public User(String name, boolean drivingLicence) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = name.toLowerCase().replace(" ", ".") + "@rental.com";
        this.password = "password123"; // Default password
        this.drivingLicence = drivingLicence;
        this.role = UserRole.CUSTOMER;
        this.assignedStoreId = null;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean hasDrivingLicence() {
        return drivingLicence;
    }

    public UserRole getRole() {
        return role;
    }

    public UUID getAssignedStoreId() {
        return assignedStoreId;
    }

    // Setters
    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setAssignedStoreId(UUID assignedStoreId) {
        this.assignedStoreId = assignedStoreId;
    }

    public void setDrivingLicence(boolean drivingLicence) {
        this.drivingLicence = drivingLicence;
    }

    /**
     * Check if this user has a specific role.
     */
    public boolean hasRole(UserRole role) {
        return this.role == role;
    }

    /**
     * Check if this user is an admin.
     */
    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    /**
     * Check if this user is a store owner.
     */
    public boolean isStoreOwner() {
        return this.role == UserRole.STORE_OWNER;
    }

    /**
     * Check if this user is a customer.
     */
    public boolean isCustomer() {
        return this.role == UserRole.CUSTOMER;
    }
}
