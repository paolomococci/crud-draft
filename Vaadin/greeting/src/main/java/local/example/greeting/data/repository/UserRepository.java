package local.example.greeting.data.repository;

import local.example.greeting.data.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
        extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}