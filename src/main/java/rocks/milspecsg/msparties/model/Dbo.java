package rocks.milspecsg.msparties.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.Calendar;
import java.util.Date;

public abstract class Dbo implements ObjectWithId<ObjectId> {

    @Id
    private ObjectId id;

    private Calendar updatedUtc;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Calendar getUpdatedUtc() {
        return updatedUtc;
    }
}
