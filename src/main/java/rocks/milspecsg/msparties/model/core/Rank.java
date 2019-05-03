package rocks.milspecsg.msparties.model.core;

import org.mongodb.morphia.annotations.Embedded;
import rocks.milspecsg.msparties.model.Dbo;

@Embedded(value = "rank")
public class Rank extends Dbo {

    // TODO: find better name
    int getIndex() {
        return -1;
    }

    String getName() {
        return "";
    }

}
