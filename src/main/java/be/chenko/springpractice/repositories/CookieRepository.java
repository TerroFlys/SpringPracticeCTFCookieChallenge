package be.chenko.springpractice.repositories;

import be.chenko.springpractice.models.Cookie;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CookieRepository extends CrudRepository<Cookie, Integer> {
    Optional<Cookie> findByNameAllIgnoreCase(String name);
}
