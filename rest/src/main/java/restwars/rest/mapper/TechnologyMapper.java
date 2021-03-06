package restwars.rest.mapper;

import com.google.common.base.Preconditions;
import restwars.model.technology.Technology;
import restwars.restapi.dto.technology.TechnologyResponse;

/**
 * Maps technology entities to DTOs and vice versa.
 */
public final class TechnologyMapper {
    private TechnologyMapper() {
    }

    public static TechnologyResponse fromTechnology(Technology technology) {
        Preconditions.checkNotNull(technology, "technology");

        return new TechnologyResponse(technology.getType().toString(), technology.getLevel());
    }
}
