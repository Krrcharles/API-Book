package fr.ensai.demo.repository;

import fr.ensai.demo.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
    public Iterable<Country> findByName(String name);

    public Iterable<Country> findByNameIgnoreCase(String name);
}
