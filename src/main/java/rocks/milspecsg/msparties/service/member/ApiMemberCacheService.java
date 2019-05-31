package rocks.milspecsg.msparties.service.member;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.member.MemberCacheService;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.service.ApiRepositoryCacheService;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class ApiMemberCacheService<M extends Member> extends ApiRepositoryCacheService<M> implements MemberCacheService<M> {

    @Inject
    public ApiMemberCacheService(ConfigurationService configurationService) {
        super(configurationService);
    }

    @Override
    public Optional<M> getOne(UUID userUUID) {
        return getOne(member -> member.userUUID.equals(userUUID));
    }
}
