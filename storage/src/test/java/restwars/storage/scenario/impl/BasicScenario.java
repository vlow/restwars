package restwars.storage.scenario.impl;

import restwars.model.building.Building;
import restwars.model.building.BuildingType;
import restwars.model.planet.Location;
import restwars.model.planet.Planet;
import restwars.model.player.Player;
import restwars.model.resource.Resources;
import restwars.model.ship.Hangar;
import restwars.model.ship.Ship;
import restwars.model.ship.ShipType;
import restwars.model.ship.Ships;
import restwars.model.technology.Technology;
import restwars.model.technology.TechnologyType;
import restwars.storage.scenario.AbstractFreemarkerScenario;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BasicScenario extends AbstractFreemarkerScenario<BasicScenario.Model> {
    public static final long CURRENT_ROUND = 10;

    public static class Player1 {
        public static final Player PLAYER = new Player(UUID.fromString("60f19086-8daa-4c02-ac41-35f7e2727d99"), "player1", "player1");
        public static class Planet1 {
            public static final Planet PLANET = new Planet(UUID.fromString("ab8657a1-f418-420a-a44a-ab5adeb7f4f1"), new Location(1, 2, 3), PLAYER.getId(), new Resources(100, 200, 300));
            public static final Building COMMAND_CENTER = new Building(UUID.fromString("701a4465-eefb-4db7-8054-5a54c6ee182e"), BuildingType.COMMAND_CENTER, 1, PLANET.getId());
            public static final Building SHIPYARD = new Building(UUID.fromString("f7a42fbe-0abc-43d3-b78e-96ca52960d54"), BuildingType.SHIPYARD, 2, PLANET.getId());
            public static final Hangar HANGAR = new Hangar(UUID.fromString("f092a0c0-a995-11e4-bcd8-0800200c9a66"), PLANET.getId(), PLAYER.getId(), new Ships(new Ship(ShipType.MOSQUITO, 5), new Ship(ShipType.COLONY, 1)));
        }

        public static class Planet2 {
            public static final Planet PLANET = new Planet(UUID.fromString("d7bf50da-c073-4d9e-80b7-093297aefc20"), new Location(3, 2, 1), PLAYER.getId(), new Resources(10, 20, 30));
            public static final Building COMMAND_CENTER = new Building(UUID.fromString("38ef6b33-aa04-43f5-8c28-a457518d0bc2"), BuildingType.COMMAND_CENTER, 2, PLANET.getId());
            public static final Building CRYSTAL_MINE = new Building(UUID.fromString("5bb08dfb-1106-46c1-91ed-38f7e8be99da"), BuildingType.CRYSTAL_MINE, 3, PLANET.getId());
        }

        public static Technology TECHNOLOGY_1 = new Technology(UUID.fromString("386222d0-a979-11e4-bcd8-0800200c9a66"), TechnologyType.BUILDING_BUILD_COST_REDUCTION, 2, PLAYER.getId());
        public static Technology TECHNOLOGY_2 = new Technology(UUID.fromString("6605fc10-a97a-11e4-bcd8-0800200c9a66"), TechnologyType.COMBUSTION_ENGINE, 3, PLAYER.getId());
    }

    public static class Player2 {
        public static final Player PLAYER = new Player(UUID.fromString("da0bace0-a97a-11e4-bcd8-0800200c9a66"), "player2", "player2");

        public static class Planet1 {
            public static final Planet PLANET = new Planet(UUID.fromString("807d6730-a999-11e4-bcd8-0800200c9a66"), new Location(4, 4, 4), PLAYER.getId(), new Resources(100, 200, 300));
        }

        public static Technology TECHNOLOGY_1 = new Technology(UUID.fromString("65e1b5c0-a97b-11e4-bcd8-0800200c9a66"), TechnologyType.BUILDING_BUILD_COST_REDUCTION, 2, PLAYER.getId());
    }

    public static class Model {
        private final long currentRound;
        private final List<Planet> planets;
        private final List<Player> players;
        private final List<Building> buildings;
        private final List<Technology> technologies;
        private final List<Hangar> hangars;

        public Model(long currentRound, List<Planet> planets, List<Player> players, List<Building> buildings, List<Technology> technologies, List<Hangar> hangars) {
            this.currentRound = currentRound;
            this.planets = planets;
            this.players = players;
            this.buildings = buildings;
            this.technologies = technologies;
            this.hangars = hangars;
        }

        public List<Planet> getPlanets() {
            return planets;
        }

        public List<Player> getPlayers() {
            return players;
        }

        public List<Building> getBuildings() {
            return buildings;
        }

        public List<Technology> getTechnologies() {
            return technologies;
        }

        public List<Hangar> getHangars() {
            return hangars;
        }

        public long getCurrentRound() {
            return currentRound;
        }
    }

    private static final BasicScenario INSTANCE = new BasicScenario();

    public static BasicScenario create() {
        return INSTANCE;
    }

    @Override
    protected Model getModel() {
        List<Player> players = Arrays.asList(
                Player1.PLAYER, Player2.PLAYER
        );
        List<Planet> planets = Arrays.asList(
                Player1.Planet1.PLANET, Player1.Planet2.PLANET, Player2.Planet1.PLANET
        );
        List<Building> buildings = Arrays.asList(
                Player1.Planet1.COMMAND_CENTER, Player1.Planet1.SHIPYARD,
                Player1.Planet2.COMMAND_CENTER, Player1.Planet2.CRYSTAL_MINE
        );
        List<Technology> technologies = Arrays.asList(
                Player1.TECHNOLOGY_1, Player1.TECHNOLOGY_2,
                Player2.TECHNOLOGY_1
        );
        List<Hangar> hangars = Arrays.asList(
                Player1.Planet1.HANGAR
        );

        return new Model(CURRENT_ROUND, planets, players, buildings, technologies, hangars);
    }

    @Override
    protected String getTemplateName() {
        return "scenario/basic.sql.ftl";
    }
}
