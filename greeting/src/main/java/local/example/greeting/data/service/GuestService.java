package local.example.greeting.data.service;

import local.example.greeting.data.entity.Guest;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public class GuestService {
    public Optional<Object> list(PageRequest of) {
        return null;
    }

    public Optional<Guest> get(Integer integer) {
        return null;
    }

    public void update(Guest guest) {
    }
}
