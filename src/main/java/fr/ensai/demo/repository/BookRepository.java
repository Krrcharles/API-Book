package fr.ensai.demo.repository;

import fr.ensai.demo.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    public Iterable<Book> findByTitle(String title);
    public Iterable<Book> findByTitleIgnoreCase(String title);
    // TODO : Add more derived queries ?
}
