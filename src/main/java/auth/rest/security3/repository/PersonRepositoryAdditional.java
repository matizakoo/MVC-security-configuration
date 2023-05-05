package auth.rest.security3.repository;

import auth.rest.security3.domain.Person;

import java.util.List;

public interface PersonRepositoryAdditional {
    String create(Person person);
    List<Person> findAll();
}
