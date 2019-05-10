package rocks.milspecsg.msparties.commands;

@FunctionalInterface
public interface CommandRegisterer {

    void register(Object plugin);
}
