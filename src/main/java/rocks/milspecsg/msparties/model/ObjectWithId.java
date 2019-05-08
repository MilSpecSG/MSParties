package rocks.milspecsg.msparties.model;

import java.util.Calendar;

public interface ObjectWithId<TKey> {

    TKey getId();
    void setId(TKey id);

    Calendar getUpdatedUtc();
}