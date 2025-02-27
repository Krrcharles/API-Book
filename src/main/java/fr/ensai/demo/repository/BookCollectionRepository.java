package fr.ensai.demo.repository;

import fr.ensai.demo.model.BookCollection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCollectionRepository extends CrudRepository<BookCollection, Long> {
    public Iterable<BookCollection> findByName(String name);

    public Iterable<BookCollection> findByNameIgnoreCase(String name);

    public Iterable<BookCollection> findByBooks_BookId(Long bookId);
}
