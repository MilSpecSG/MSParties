package rocks.milspecsg.msparties.service.member;

import com.google.inject.Inject;
import rocks.milspecsg.msparties.api.config.ConfigurationService;
import rocks.milspecsg.msparties.api.member.MemberCacheService;
import rocks.milspecsg.msparties.model.core.Member;
import rocks.milspecsg.msparties.service.ApiRepositoryCacheService;

import java.util.Optional;
import java.util.UUID;

public class ApiMemberCacheService extends ApiRepositoryCacheService<Member> implements MemberCacheService {

    @Inject
    public ApiMemberCacheService(ConfigurationService configurationService) {
        super(configurationService);
    }

    @Override
    public Optional<? extends Member> getOne(UUID userUUID) {
        return getOne(member -> member.userUUID.equals(userUUID));
    }
}
