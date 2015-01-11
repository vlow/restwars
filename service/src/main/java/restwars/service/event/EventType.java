package restwars.service.event;

public enum EventType {
    BUILDING_COMPLETED(0),
    RESEARCH_COMPLETED(1),
    SHIP_COMPLETED(2),
    FLIGHT_RETURNED(3),
    PLANET_COLONIZED(4),
    TRANSPORT_ARRIVED(5),
    FIGHT_HAPPENED(6),
    TRANSFER_ARRIVED(7);

    private final int id;

    EventType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EventType fromId(int id) {
        for (EventType buildingType : EventType.values()) {
            if (buildingType.getId() == id) {
                return buildingType;
            }
        }

        throw new IllegalArgumentException("Unknown id: " + id);
    }
}
