package rocks.milspecsg.msparties.api.party;

import com.google.inject.Singleton;

import java.util.List;
import java.util.function.Predicate;

public interface NameVerificationService {

    /**
     * Every filter must return true in order for string to pass
     */
    List<Predicate<String>> getFilters();

    void addFilter(Predicate<String> predicate);

    void setFilters(List<Predicate<String>> filters);

    /**
     *
     * @param value {@code String} to check
     * @return true if string is passes all filters
     */
    boolean isOk(String value);


}
