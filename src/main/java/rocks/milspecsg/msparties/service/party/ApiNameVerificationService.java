package rocks.milspecsg.msparties.service.party;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import rocks.milspecsg.msparties.api.party.NameVerificationService;
import rocks.milspecsg.msparties.api.party.PartyCacheService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Singleton
public class ApiNameVerificationService implements NameVerificationService {

    private List<Predicate<String>> filters;

    @Inject
    public ApiNameVerificationService() {
        this.filters = new ArrayList<>();
    }

    @Override
    public List<Predicate<String>> getFilters() {
        return this.filters;
    }

    @Override
    public void addFilter(Predicate<String> predicate) {
        this.filters.add(predicate);
    }

    @Override
    public void setFilters(List<Predicate<String>> filters) {
        this.filters = filters;
    }

    @Override
    public boolean isOk(String value) {
        for (Predicate<String> predicate : filters)
            if (!predicate.test(value)) return false;
        return true;
    }

}
