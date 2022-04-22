package local.example.hail.data.repository;

import local.example.hail.data.entity.Guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository
        extends JpaRepository<Guest, Long> {

}