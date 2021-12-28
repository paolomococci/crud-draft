package local.example.hail.data.endpoint;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.fusion.Endpoint;

import java.util.Optional;

import local.example.hail.data.entity.User;
import local.example.hail.security.AuthenticatedUser;

import org.springframework.beans.factory.annotation.Autowired;

@Endpoint
@AnonymousAllowed
public class UserEndpoint {

    @Autowired
    private AuthenticatedUser authenticatedUser;

    public Optional<User> getAuthenticatedUser() {
        return authenticatedUser.get();
    }
}
