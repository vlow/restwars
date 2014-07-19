package restwars.service.technology;

import restwars.service.planet.Planet;
import restwars.service.player.Player;
import restwars.service.resource.InsufficientResourcesException;
import restwars.service.resource.Resources;

import java.util.List;
import java.util.Optional;

/**
 * Service to manage technologies.
 */
public interface TechnologyService {
    /**
     * Researches the given technology on the given planet.
     *
     * @param player     Player.
     * @param planet     Planet.
     * @param technology Technology to research.
     * @return Research.
     */
    Research researchTechnology(Player player, Planet planet, TechnologyType technology) throws InsufficientResourcesException;

    /**
     * Calculates the research time for the given type and level of technology.
     *
     * @param type  Type of technology.
     * @param level Level to research.
     * @return Research time in rounds.
     */
    long calculateResearchTime(TechnologyType type, int level);

    /**
     * Calculates the research cost for the given type and level of technology.
     *
     * @param type  Type of technology.
     * @param level Level to research.
     * @return Research cost.
     */
    Resources calculateResearchCost(TechnologyType type, int level);

    /**
     * Finds all technologies for a given player.
     *
     * @param player Player.
     * @return All technologies for the given player.
     */
    List<Technology> findAllForPlayer(Player player);

    /**
     * Finds a technology with a given type for a given player.
     *
     * @param player Player.
     * @param type   Type of technology.
     * @return Technology for the given player.
     */
    Optional<Technology> findForPlayer(Player player, TechnologyType type);
}
