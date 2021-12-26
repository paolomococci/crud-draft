package local.example.greeting.data.service;

import local.example.greeting.data.entity.Guest;
import local.example.greeting.data.repository.GuestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class GuestService {

    @Autowired
    GuestRepository guestRepository;

    public Optional<Guest> read(Integer id) {
        return this.guestRepository.findById(id);
    }

    public Page<Guest> paginate(Pageable pageable) {
        return this.guestRepository.findAll(pageable);
    }

    public Guest update(Guest guest) {
        return this.guestRepository.save(guest);
    }
}
