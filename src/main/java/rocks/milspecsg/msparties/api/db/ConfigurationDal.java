package rocks.milspecsg.msparties.api.db;

public interface ConfigurationDal {

    Object getBaseNode();

    void load();
}
