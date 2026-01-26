package vehiclerentalsystem.lock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for lock operations.
 * Provides a clean API for executing code within a lock.
 * 
 * Usage:
 * 
 * <pre>
 * LockTemplate.executeWithLock(lockService, vehicleId, 5, TimeUnit.SECONDS, () -> {
 *     // This code runs while holding the lock
 *     reserveVehicle(vehicleId);
 * });
 * </pre>
 */
public class LockTemplate {

    /**
     * Execute a task while holding a lock.
     * Automatically releases the lock when done (even if exception occurs).
     * 
     * @param lockService The lock service to use
     * @param resourceId  Resource to lock
     * @param timeout     Lock timeout
     * @param unit        Time unit
     * @param task        Task to execute
     * @throws LockAcquisitionException if lock cannot be acquired
     */
    public static void executeWithLock(LockService lockService, UUID resourceId,
            long timeout, TimeUnit unit, Runnable task) {
        boolean acquired = false;
        try {
            acquired = lockService.acquireLock(resourceId, timeout, unit);
            if (!acquired) {
                throw new LockAcquisitionException(
                        "Could not acquire lock for resource: " + resourceId +
                                ". Resource may be in use by another operation.");
            }
            // Execute the task while holding the lock
            task.run();
        } finally {
            if (acquired) {
                lockService.releaseLock(resourceId);
            }
        }
    }

    /**
     * Execute a task that returns a value while holding a lock.
     */
    public static <T> T executeWithLock(LockService lockService, UUID resourceId,
            long timeout, TimeUnit unit,
            java.util.function.Supplier<T> task) {
        boolean acquired = false;
        try {
            acquired = lockService.acquireLock(resourceId, timeout, unit);
            if (!acquired) {
                throw new LockAcquisitionException(
                        "Could not acquire lock for resource: " + resourceId);
            }
            return task.get();
        } finally {
            if (acquired) {
                lockService.releaseLock(resourceId);
            }
        }
    }
}
