package restwars.rest.resources;

import com.google.common.base.Preconditions;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import io.dropwizard.auth.Auth;
import restwars.rest.api.building.BuildingResponse;
import restwars.rest.resources.param.LocationParam;
import restwars.rest.util.Helper;
import restwars.service.building.Building;
import restwars.service.building.BuildingService;
import restwars.service.planet.Planet;
import restwars.service.planet.PlanetService;
import restwars.service.player.Player;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import java.util.List;

@Api(value = "/{location}/building", hidden = true)
public class BuildingSubResource {
    private final PlanetService planetService;
    private final BuildingService buildingService;

    @Inject
    public BuildingSubResource(BuildingService buildingService, PlanetService planetService) {
        this.planetService = Preconditions.checkNotNull(planetService, "planetService");
        this.buildingService = Preconditions.checkNotNull(buildingService, "buildingService");
    }

    @GET
    @ApiOperation(value = "Get all buildings on a planet")
    public List<BuildingResponse> getBuildings(
            @Auth @ApiParam(access = "internal") Player player,
            @PathParam("location") @ApiParam("Planet location") LocationParam location
    ) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(location, "location");

        Planet planet = Helper.findPlanetWithLocationAndOwner(planetService, location.getValue(), player);
        List<Building> buildings = buildingService.findBuildingsOnPlanet(planet);

        return Helper.mapToList(buildings, BuildingResponse::fromBuilding);
    }
}
