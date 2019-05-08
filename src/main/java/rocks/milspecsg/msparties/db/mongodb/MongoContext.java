package rocks.milspecsg.msparties.db.mongodb;

import com.google.inject.Inject;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.model.core.Party;
import rocks.milspecsg.msparties.model.core.Rank;

public class MongoContext {

    public final Datastore datastore;

    @Inject
    public MongoContext() {
//        MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/",
//                MongoClientOptions.builder().sslEnabled(true));
        MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/");
        MongoClient mongoClient = new MongoClient(uri);

        Morphia morphia = new Morphia();
        morphia.map(
                Member.class,
                Party.class,
                Rank.class
        );

        datastore = morphia.createDatastore(mongoClient, "msparties");
        datastore.ensureIndexes();
    }
}
