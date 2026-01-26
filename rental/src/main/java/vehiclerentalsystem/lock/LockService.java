package vehiclerentalsystem.lock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Distributed Lock Service Interface.
 * 
 * Provides locking mechanism to prevent race conditions during:
 * - Vehicle reservations (prevent double-booking)
 * - Inventory updates
 * - Any concurrent operations on shared resources
 * 
 * In production, this would be implemented using:
 * - Redis (RedLock algorithm)
 * - Database locks (SELECT FOR UPDATE)
 * - ZooKeeper
 * 
 * For development, we use an in-memory implementation.
 */
public interface LockService {

    /**
     * Acquire a lock for a specific resource.
     * Blocks until lock is acquired or timeout expires.
     * 
     * @param resourceId Unique identifier for the resource (e.g., vehicleId)
     * @param timeout    Maximum time to wait for lock
     * @param unit       Time unit for timeout
     * @return true if lock was acquired, false if timeout
     */
    boolean acquireLock(UUID resourceId, long timeout, TimeUnit unit);

    /**
     * Try to acquire a lock without waiting.
     * Returns immediately.
     * 
     * @param resourceId Unique identifier for the resource
     * @return true if lock was acquired, false if resource is already locked
     */
    boolean tryLock(UUID resourceId);

    /**
     * Release a previously acquired lock.
     * 
     * @param resourceId Unique identifier for the resource
     */
    void releaseLock(UUID resourceId);

    /**
     * Check if a resource is currently locked.
     * 
     * @param resourceId Unique identifier for the resource
     * @return true if resource is locked
     */
    boolean isLocked(UUID resourceId);
}
