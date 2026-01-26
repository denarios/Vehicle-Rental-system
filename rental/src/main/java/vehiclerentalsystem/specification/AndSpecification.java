package vehiclerentalsystem.specification;

/**
 * AND Specification - combines two specifications with AND logic.
 * Both specifications must be satisfied for this to return true.
 */
public class AndSpecification<T> implements Specification<T> {

    private final Specification<T> left;
    private final Specification<T> right;

    public AndSpecification(Specification<T> left, Specification<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfiedBy(T candidate) {
        return left.isSatisfiedBy(candidate) && right.isSatisfiedBy(candidate);
    }

    @Override
    public String toString() {
        return "(" + left + " AND " + right + ")";
    }
}
