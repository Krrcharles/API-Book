package fr.ensai.demo.repository;

import fr.ensai.demo.model.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
    Iterable<Author> findByName(String name);

    Iterable<Author> findByNameIgnoreCase(String name);
}
