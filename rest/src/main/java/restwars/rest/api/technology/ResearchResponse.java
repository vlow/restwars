package restwars.rest.api.technology;

import com.google.common.base.Preconditions;
import restwars.service.technology.Research;

public class ResearchResponse {
    private final String type;

    private final int level;

    private final long started;

    private final long done;

    public ResearchResponse(String type, int level, long started, long done) {
        this.type = Preconditions.checkNotNull(type, "type");
        this.level = level;
        this.started = started;
        this.done = done;
    }

    public String getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public long getStarted() {
        return started;
    }

    public long getDone() {
        return done;
    }

    public static ResearchResponse fromResearch(Research research) {
        Preconditions.checkNotNull(research, "research");

        return new ResearchResponse(research.getType().toString(), research.getLevel(), research.getStarted(), research.getDone());
    }

}