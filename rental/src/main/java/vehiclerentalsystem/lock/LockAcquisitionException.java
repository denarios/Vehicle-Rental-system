package vehiclerentalsystem.lock;

/**
 * Exception thrown when a lock cannot be acquired.
 * 
 * This typically happens when:
 * - Another thread/request is processing the same resource
 * - Lock timeout expired
 * - System is under heavy load
 */
public class LockAcquisitionException extends RuntimeException {

    public LockAcquisitionException(String message) {
        super(message);
    }

    public LockAcquisitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
