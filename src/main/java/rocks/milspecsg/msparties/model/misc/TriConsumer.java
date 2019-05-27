package rocks.milspecsg.msparties.model.misc;

@FunctionalInterface
public interface TriConsumer<A, B, C> {

    void accept(A a, B b, C c);
}
