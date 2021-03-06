package restwars.rest.integration.database;

import com.google.common.base.Preconditions;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.spi.container.ResourceMethodDispatchProvider;
import com.sun.jersey.spi.dispatch.RequestDispatcher;
import restwars.service.unitofwork.UnitOfWorkService;

/**
 * Provider which wires the UnitOfWorkRequestDispatcher and the UnitOfWorkService together.
 */
public class UnitOfWorkResourceMethodDispatchProvider implements ResourceMethodDispatchProvider {
    private final UnitOfWorkService unitOfWorkService;
    private final ResourceMethodDispatchProvider provider;

    public UnitOfWorkResourceMethodDispatchProvider(UnitOfWorkService unitOfWorkService, ResourceMethodDispatchProvider provider) {
        this.unitOfWorkService = Preconditions.checkNotNull(unitOfWorkService, "unitOfWorkService");
        this.provider = Preconditions.checkNotNull(provider, "provider");
    }

    @Override
    public RequestDispatcher create(AbstractResourceMethod abstractResourceMethod) {
        final RequestDispatcher dispatcher = provider.create(abstractResourceMethod);

        return new UnitOfWorkRequestDispatcher(unitOfWorkService, dispatcher);
    }
}
