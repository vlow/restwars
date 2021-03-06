package restwars.rest;

import com.google.common.base.Preconditions;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restwars.UnrecoverableException;
import restwars.model.UniverseConfiguration;
import restwars.service.building.BuildingService;
import restwars.service.flight.FlightService;
import restwars.service.infrastructure.RoundService;
import restwars.service.resource.ResourceService;
import restwars.service.ship.ShipService;
import restwars.service.technology.TechnologyService;
import restwars.service.telescope.TelescopeService;
import restwars.service.unitofwork.UnitOfWorkService;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Clock implements Managed, Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Clock.class);

    private final BuildingService buildingService;
    private final RoundService roundService;
    private final ResourceService resourceService;
    private final TechnologyService technologyService;
    private final ShipService shipService;
    private final FlightService flightService;
    private final TelescopeService telescopeService;
    private final UniverseConfiguration universeConfiguration;

    private final UnitOfWorkService unitOfWorkService;

    @Nullable
    private ScheduledExecutorService scheduledExecutorService;

    @Inject
    public Clock(BuildingService buildingService, RoundService roundService, UniverseConfiguration universeConfiguration, ResourceService resourceService, TechnologyService technologyService, ShipService shipService, UnitOfWorkService unitOfWorkService, TelescopeService telescopeService, FlightService flightService) {
        this.flightService = Preconditions.checkNotNull(flightService, "flightService");
        this.unitOfWorkService = Preconditions.checkNotNull(unitOfWorkService, "unitOfWorkService");
        this.shipService = Preconditions.checkNotNull(shipService, "shipService");
        this.technologyService = Preconditions.checkNotNull(technologyService, "technologyService");
        this.resourceService = Preconditions.checkNotNull(resourceService, "resourceService");
        this.universeConfiguration = Preconditions.checkNotNull(universeConfiguration, "universeConfiguration");
        this.roundService = Preconditions.checkNotNull(roundService, "roundService");
        this.buildingService = Preconditions.checkNotNull(buildingService, "buildingService");
        this.telescopeService = Preconditions.checkNotNull(telescopeService, "telescopeService");
    }

    @Override
    public void start() throws Exception {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(); // TODO: Code smell - IoC
        // TODO: Decision - Maybe change this to .scheduleWithFixedRate. Maybe this could lead to multithreading issues?
        scheduledExecutorService.scheduleWithFixedDelay(this, universeConfiguration.getRoundTimeInSeconds(), universeConfiguration.getRoundTimeInSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws Exception {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS);
            scheduledExecutorService.shutdownNow();
        }
    }

    @Override
    public void run() {
        unitOfWorkService.start();
        try {
            long round = roundService.nextRound();
            LOGGER.info("Starting round {}", round);

            buildingService.finishConstructionSites();
            technologyService.finishResearches();
            shipService.finishShipsInConstruction();
            resourceService.gatherResourcesOnAllPlanets();
            flightService.finishFlights();
            telescopeService.detectFlights();

            unitOfWorkService.commit();
        } catch (Exception e) {
            unitOfWorkService.abort();
            LOGGER.error("Clock thread crashed with exception", e);
            throw new UnrecoverableException("Exception", e);
        }
    }
}
