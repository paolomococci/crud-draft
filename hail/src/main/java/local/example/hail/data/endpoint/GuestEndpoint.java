package local.example.hail.data.endpoint;

import com.vaadin.fusion.Endpoint;
import com.vaadin.fusion.Nonnull;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import local.example.hail.data.entity.Guest;
import local.example.hail.data.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Endpoint
@RolesAllowed("admin")
public class GuestEndpoint {

    private GuestService service;

    public GuestEndpoint(@Autowired GuestService service) {
        this.service = service;
    }

    @Nonnull
    public Page<@Nonnull Guest> list(Pageable page) {
        return service.list(page);
    }

    public Optional<Guest> get(@Nonnull Long id) {
        return service.get(id);
    }

    @Nonnull
    public Guest update(@Nonnull Guest entity) {
        return service.update(entity);
    }

    public void delete(@Nonnull Long id) {
        service.delete(id);
    }

    public int count() {
        return service.count();
    }
}
