package vehiclerentalsystem.enums;

/**
 * User roles for role-based access control (RBAC).
 * 
 * CUSTOMER:
 * - Can browse vehicles by location
 * - Can create/view their own reservations
 * - Can update their profile
 * 
 * STORE_OWNER:
 * - Can view analytics for their assigned store
 * - Can manage vehicles for their store
 * - Can view reservations for their store
 * - Cannot access other stores
 * 
 * ADMIN:
 * - Full access to all features
 * - Can create stores and assign store owners
 * - Can manage all stores, vehicles, users
 * - Can view analytics across all stores
 */
public enum UserRole {
    CUSTOMER,
    STORE_OWNER,
    ADMIN
}
