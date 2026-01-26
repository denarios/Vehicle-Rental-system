package vehiclerentalsystem.specification;

/**
 * OR Specification - combines two specifications with OR logic.
 * At least one specification must be satisfied for this to return true.
 */
public class OrSpecification<T> implements Specification<T> {

    private final Specification<T> left;
    private final Specification<T> right;

    public OrSpecification(Specification<T> left, Specification<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfiedBy(T candidate) {
        return left.isSatisfiedBy(candidate) || right.isSatisfiedBy(candidate);
    }

    @Override
    public String toString() {
        return "(" + left + " OR " + right + ")";
    }
}
