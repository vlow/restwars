package restwars.rest.resources;

import com.google.common.base.Preconditions;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import io.dropwizard.auth.Auth;
import restwars.model.planet.Planet;
import restwars.model.player.Player;
import restwars.model.technology.Research;
import restwars.model.technology.TechnologyType;
import restwars.rest.mapper.ResearchMapper;
import restwars.rest.resources.param.LocationParam;
import restwars.restapi.dto.technology.ResearchRequest;
import restwars.restapi.dto.technology.ResearchResponse;
import restwars.service.planet.PlanetService;
import restwars.service.technology.ResearchException;
import restwars.service.technology.TechnologyService;
import restwars.util.Functional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/{location}/research", hidden = true)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ResearchSubResource {
    private final TechnologyService technologyService;
    private final PlanetService planetService;

    @Inject
    public ResearchSubResource(TechnologyService technologyService, PlanetService planetService) {
        this.planetService = Preconditions.checkNotNull(planetService, "planetService");
        this.technologyService = Preconditions.checkNotNull(technologyService, "technologyService");
    }

    @GET
    @ApiOperation("Lists all running researches on a planet")
    public List<ResearchResponse> getResearch(
            @Auth @ApiParam(access = "internal") Player player,
            @PathParam("location") @ApiParam("Planet location") LocationParam location
    ) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(location, "location");

        Planet planet = ResourceHelper.findPlanetWithLocationAndOwner(planetService, location.getValue(), player);
        List<Research> researches = technologyService.findResearchesOnPlanet(planet);

        return Functional.mapToList(researches, ResearchMapper::fromResearch);
    }

    @POST
    @ApiOperation("Researches a new technology")
    public ResearchResponse research(
            @Auth @ApiParam(access = "internal") Player player,
            @PathParam("location") @ApiParam("Planet location") LocationParam location,
            @Valid ResearchRequest data
    ) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(location, "location");
        Preconditions.checkNotNull(data, "data");

        Planet planet = ResourceHelper.findPlanetWithLocationAndOwner(planetService, location.getValue(), player);

        TechnologyType type = ResourceHelper.parseTechnologyType(data.getType());
        try {
            Research research = technologyService.researchTechnology(player, planet, type);
            return ResearchMapper.fromResearch(research);
        } catch (ResearchException e) {
            throw new ResearchWebException(e.getReason());
        }
    }
}
