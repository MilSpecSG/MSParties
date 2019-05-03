package rocks.milspecsg.msparties.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.Date;

public abstract class Dbo implements ObjectWithId<ObjectId, Date> {

    @Id
    private ObjectId id;

    private Date updatedUtc;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Date getUpdatedUtc() {
        return updatedUtc;
    }

    public void setUpdatedUtc(Date updatedUtc) {
        this.updatedUtc = updatedUtc;
    }
}
