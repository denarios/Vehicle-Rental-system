package vehiclerentalsystem.specification;

/**
 * Specification Pattern interface for building composable business rules.
 * 
 * This pattern allows:
 * - Encapsulating filter logic in reusable classes
 * - Combining specifications using and(), or(), not()
 * - Clean, testable search/filter implementations
 * 
 * @param <T> The type of object being evaluated
 */
public interface Specification<T> {

    /**
     * Check if the candidate satisfies this specification.
     * 
     * @param candidate The object to evaluate
     * @return true if the candidate satisfies this specification
     */
    boolean isSatisfiedBy(T candidate);

    /**
     * Combine this specification with another using AND logic.
     * Both specifications must be satisfied.
     * 
     * @param other The other specification
     * @return A new specification representing (this AND other)
     */
    default Specification<T> and(Specification<T> other) {
        return new AndSpecification<>(this, other);
    }

    /**
     * Combine this specification with another using OR logic.
     * At least one specification must be satisfied.
     * 
     * @param other The other specification
     * @return A new specification representing (this OR other)
     */
    default Specification<T> or(Specification<T> other) {
        return new OrSpecification<>(this, other);
    }

    /**
     * Negate this specification.
     * 
     * @return A new specification representing (NOT this)
     */
    default Specification<T> not() {
        return new NotSpecification<>(this);
    }
}
