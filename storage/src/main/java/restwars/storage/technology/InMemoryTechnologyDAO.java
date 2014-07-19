package restwars.storage.technology;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import restwars.service.technology.Technology;
import restwars.service.technology.TechnologyDAO;
import restwars.service.technology.TechnologyType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class InMemoryTechnologyDAO implements TechnologyDAO {
    private final Map<UUID, Technology> technologies = Maps.newHashMap();

    @Override
    public List<Technology> findAllWithPlayerId(UUID playerId) {
        Preconditions.checkNotNull(playerId, "playerId");

        return technologies.values().stream().filter(t -> t.getPlayerId().equals(playerId)).collect(Collectors.toList());
    }

    @Override
    public Optional<Technology> findWithPlayerId(UUID playerId, TechnologyType type) {
        return technologies.values().stream().filter(t -> t.getPlayerId().equals(playerId) && t.getType().equals(type)).findFirst();
    }
}
