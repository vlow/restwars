package restwars.rest.di;

import com.google.common.base.Preconditions;
import dagger.Module;
import dagger.Provides;
import io.dropwizard.db.ManagedDataSource;
import restwars.model.UniverseConfiguration;
import restwars.rest.CompositionRoot;
import restwars.service.di.MechanicsModule;
import restwars.service.di.ServiceModule;
import restwars.service.unitofwork.UnitOfWorkService;
import restwars.storage.di.JooqDAOModule;
import restwars.storage.jooq.JooqUnitOfWorkFactory;
import restwars.storage.jooq.JooqUnitOfWorkService;

import javax.inject.Named;

/**
 * Dagger module for RESTwars.
 */
@Module(injects = CompositionRoot.class, includes = {
        JooqDAOModule.class, ServiceModule.class, MechanicsModule.class
})
public class RestWarsModule {
    private final UniverseConfiguration universeConfiguration;
    private final ManagedDataSource managedDataSource;
    private final int passwordIterations;

    public RestWarsModule(UniverseConfiguration universeConfiguration, ManagedDataSource managedDataSource, int passwordIterations) {
        this.managedDataSource = Preconditions.checkNotNull(managedDataSource, "managedDataSource");
        this.universeConfiguration = Preconditions.checkNotNull(universeConfiguration, "universeConfiguration");
        this.passwordIterations = passwordIterations;
    }

    @Provides
    UnitOfWorkService providesUnitOfWorkService(JooqUnitOfWorkFactory jooqUnitOfWorkFactory) {
        return new JooqUnitOfWorkService(managedDataSource, jooqUnitOfWorkFactory);
    }

    @Provides
    UniverseConfiguration providesUniverseConfiguration() {
        return universeConfiguration;
    }

    @Provides
    @Named("passwordIterations")
    int providesPasswordIterations() {
        return passwordIterations;
    }
}
