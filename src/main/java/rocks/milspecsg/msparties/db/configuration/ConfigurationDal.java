package rocks.milspecsg.msparties.db.configuration;

public interface ConfigurationDal {

    Object getBaseNode();

    void load();
}
