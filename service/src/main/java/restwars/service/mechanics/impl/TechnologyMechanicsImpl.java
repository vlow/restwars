package restwars.service.mechanics.impl;

import com.google.common.base.Preconditions;
import restwars.model.resource.Resources;
import restwars.model.technology.TechnologyType;
import restwars.model.techtree.Prerequisites;
import restwars.service.mechanics.TechnologyMechanics;

public class TechnologyMechanicsImpl implements TechnologyMechanics {
    @Override
    public Resources calculateResearchCost(TechnologyType type, int level) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkArgument(level > 0, "level must be > 0");

        switch (type) {
            case BUILDING_BUILD_COST_REDUCTION:
                return new Resources(
                        200 + (level - 1) * 100,
                        100 + (level - 1) * 50,
                        800 + (level - 1) * 400
                );
            case COMBUSTION_ENGINE:
                return new Resources(
                        300 + (level - 1) * 150,
                        150 + (level - 1) * 75,
                        1200 + (level - 1) * 600
                );
            default:
                throw new IllegalArgumentException("Invalid technology: " + type);
        }
    }

    @Override
    public int calculateResearchTime(TechnologyType type, int level) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkArgument(level > 0, "level must be > 0");

        switch (type) {
            case BUILDING_BUILD_COST_REDUCTION:
                return 50 + (level - 1) * 25;
            case COMBUSTION_ENGINE:
                return 100 + (level - 1) * 50;
            default:
                throw new IllegalArgumentException("Invalid technology: " + type);
        }
    }

    @Override
    public Prerequisites getPrerequisites(TechnologyType type) {
        Preconditions.checkNotNull(type, "type");

        switch (type) {
            case BUILDING_BUILD_COST_REDUCTION:
                return Prerequisites.NONE;
            case COMBUSTION_ENGINE:
                return Prerequisites.NONE;
            default:
                throw new IllegalArgumentException("Invalid technology: " + type);
        }
    }

    @Override
    public double calculateBuildCostReduction(int level) {
        Preconditions.checkArgument(level >= 0, "level must be >= 0");

        return Math.min(1, level * 0.02);
    }

    @Override
    public double calculateCombustionFlightCostReduction(int level) {
        Preconditions.checkArgument(level >= 0, "level must be >= 0");

        return Math.min(1, level * 0.02);
    }
}
