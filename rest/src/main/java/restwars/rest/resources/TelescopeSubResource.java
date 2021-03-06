package restwars.rest.resources;

import com.google.common.base.Preconditions;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import io.dropwizard.auth.Auth;
import restwars.model.planet.Planet;
import restwars.model.player.Player;
import restwars.rest.mapper.PlanetMapper;
import restwars.rest.resources.param.LocationParam;
import restwars.restapi.dto.planet.PlanetScanResponse;
import restwars.service.planet.PlanetService;
import restwars.service.telescope.ScanException;
import restwars.service.telescope.TelescopeService;
import restwars.util.Functional;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/{location}/telescope", hidden = true)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TelescopeSubResource {
    private final PlanetService planetService;
    private final TelescopeService telescopeService;

    @Inject
    public TelescopeSubResource(TelescopeService telescopeService, PlanetService planetService) {
        this.planetService = Preconditions.checkNotNull(planetService, "planetService");
        this.telescopeService = Preconditions.checkNotNull(telescopeService, "telescopeService");
    }

    @GET
    @Path("/scan")
    @ApiOperation("Lists all planets in vicinity")
    public List<PlanetScanResponse> scan(
            @Auth @ApiParam(access = "internal") Player player,
            @PathParam("location") @ApiParam("Planet location") LocationParam location
    ) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(location, "location");

        Planet planet = ResourceHelper.findPlanetWithLocationAndOwner(planetService, location.getValue(), player);
        try {
            return Functional.mapToList(telescopeService.scan(planet), PlanetMapper::fromPlanetWithOwner);
        } catch (ScanException e) {
            throw new ScanWebException(e.getReason());
        }
    }
}
