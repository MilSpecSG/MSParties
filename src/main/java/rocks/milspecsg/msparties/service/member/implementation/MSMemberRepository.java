package rocks.milspecsg.msparties.service.member.implementation;

import com.google.inject.Inject;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import rocks.milspecsg.msparties.api.member.MemberCacheService;
import rocks.milspecsg.msparties.db.mongodb.MongoContext;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.service.member.ApiMemberRepository;

public class MSMemberRepository extends ApiMemberRepository<Member> {

    @Inject
    public MSMemberRepository(MongoContext mongoContext, MemberCacheService<Member> memberCacheService) {
        super(mongoContext, memberCacheService);
    }

    @Override
    public Member generateDefault() {
        return new Member();
    }

    @Override
    public UpdateOperations<Member> createUpdateOperations() {
        return mongoContext.datastore.createUpdateOperations(Member.class);
    }

    @Override
    public Query<Member> asQuery() {
        return mongoContext.datastore.createQuery(Member.class);
    }
}
