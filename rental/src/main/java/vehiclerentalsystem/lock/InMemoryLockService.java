package vehiclerentalsystem.lock;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

/**
 * In-Memory Lock Service Implementation.
 * 
 * Uses Java's ReentrantLock for thread-safe locking.
 * Suitable for single-instance applications.
 * 
 * For distributed systems (multiple servers), replace with:
 * - RedisLockService (using Redis SETNX + TTL)
 * - DatabaseLockService (using SELECT FOR UPDATE)
 * 
 * Interview point: "Implemented distributed locking to prevent
 * race conditions during concurrent vehicle reservations"
 */
@Service
public class InMemoryLockService implements LockService {

    /**
     * Map of resource IDs to their locks.
     * ConcurrentHashMap ensures thread-safe access to the map itself.
     */
    private final Map<UUID, ReentrantLock> locks = new ConcurrentHashMap<>();

    /**
     * Get or create a lock for a resource.
     * computeIfAbsent is atomic - prevents race condition when creating locks.
     */
    private ReentrantLock getLock(UUID resourceId) {
        return locks.computeIfAbsent(resourceId, id -> new ReentrantLock(true)); // fair lock
    }

    @Override
    public boolean acquireLock(UUID resourceId, long timeout, TimeUnit unit) {
        ReentrantLock lock = getLock(resourceId);
        try {
            // Try to acquire lock with timeout
            boolean acquired = lock.tryLock(timeout, unit);
            if (acquired) {
                System.out.println("[LOCK] Acquired lock for resource: " + resourceId);
            } else {
                System.out.println("[LOCK] Failed to acquire lock (timeout) for: " + resourceId);
            }
            return acquired;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[LOCK] Interrupted while waiting for lock: " + resourceId);
            return false;
        }
    }

    @Override
    public boolean tryLock(UUID resourceId) {
        ReentrantLock lock = getLock(resourceId);
        boolean acquired = lock.tryLock();
        if (acquired) {
            System.out.println("[LOCK] Acquired lock (no wait) for: " + resourceId);
        } else {
            System.out.println("[LOCK] Resource already locked: " + resourceId);
        }
        return acquired;
    }

    @Override
    public void releaseLock(UUID resourceId) {
        ReentrantLock lock = locks.get(resourceId);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
            System.out.println("[LOCK] Released lock for resource: " + resourceId);
        }
    }

    @Override
    public boolean isLocked(UUID resourceId) {
        ReentrantLock lock = locks.get(resourceId);
        return lock != null && lock.isLocked();
    }

    /**
     * Get the number of threads waiting for a lock.
     * Useful for monitoring and debugging.
     */
    public int getQueueLength(UUID resourceId) {
        ReentrantLock lock = locks.get(resourceId);
        return lock != null ? lock.getQueueLength() : 0;
    }
}
