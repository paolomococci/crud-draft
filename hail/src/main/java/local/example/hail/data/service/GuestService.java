package local.example.hail.data.service;

import java.util.Optional;
import local.example.hail.data.entity.Guest;
import local.example.hail.data.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GuestService {

    @Autowired
    GuestRepository repository;

    public Optional<Guest> get(Long id) {
        return repository.findById(id);
    }

    public Guest update(Guest entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Guest> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
