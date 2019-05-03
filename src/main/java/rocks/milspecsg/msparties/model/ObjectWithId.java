package rocks.milspecsg.msparties.model;

public interface ObjectWithId<TKey, TDateTime> {

    TKey getId();
    void setId(TKey id);

    TDateTime getUpdatedUtc();
    void setUpdatedUtc(TDateTime updatedUtc);
}