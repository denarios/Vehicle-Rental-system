package vehiclerentalsystem.specification;

/**
 * NOT Specification - negates a specification.
 * Returns true if the wrapped specification returns false.
 */
public class NotSpecification<T> implements Specification<T> {

    private final Specification<T> spec;

    public NotSpecification(Specification<T> spec) {
        this.spec = spec;
    }

    @Override
    public boolean isSatisfiedBy(T candidate) {
        return !spec.isSatisfiedBy(candidate);
    }

    @Override
    public String toString() {
        return "(NOT " + spec + ")";
    }
}
