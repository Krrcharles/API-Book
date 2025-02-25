package fr.ensai.demo.repository;

import fr.ensai.demo.model.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {
    Iterable<Genre> findByLabel(String label);
    Iterable<Genre> findByLabelIgnoreCase(String label);
}
