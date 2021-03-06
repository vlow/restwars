package restwars.rest.resources;

import com.google.common.base.Preconditions;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.Authorization;
import io.dropwizard.auth.Auth;
import restwars.model.planet.Planet;
import restwars.model.player.Player;
import restwars.rest.mapper.PlanetMapper;
import restwars.restapi.dto.player.PlayerResponse;
import restwars.restapi.dto.player.RegisterPlayerRequest;
import restwars.service.planet.PlanetService;
import restwars.service.player.CreatePlayerException;
import restwars.service.player.PlayerService;
import restwars.util.Functional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/v1/player")
@Api(value = "/v1/player", description = "Player management")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {
    private final PlayerService playerService;
    private final PlanetService planetService;

    @Context
    private UriInfo uriInfo;

    @Inject
    public PlayerResource(PlayerService playerService, PlanetService planetService) {
        this.planetService = Preconditions.checkNotNull(planetService, "planetService");
        this.playerService = Preconditions.checkNotNull(playerService, "playerService");
    }

    @GET
    @ApiOperation(value = "Gets information about the current player", authorizations = {
            @Authorization("basicAuth")
    })
    public PlayerResponse me(@Auth @ApiParam(access = "internal") Player player) {
        List<Planet> planets = planetService.findWithOwner(player);

        return new PlayerResponse(player.getUsername(), Functional.mapToList(planets, PlanetMapper::fromPlanet));
    }

    @POST
    @ApiOperation("Registers a new player")
    public Response register(@Valid RegisterPlayerRequest registration) {
        Preconditions.checkNotNull(registration, "registration");

        try {
            playerService.createPlayer(registration.getUsername(), registration.getPassword());

            return Response.created(uriInfo.getAbsolutePathBuilder().path("/").build()).build();
        } catch (CreatePlayerException e) {
            throw new CreatePlayerWebException(e.getReason());
        }
    }
}
