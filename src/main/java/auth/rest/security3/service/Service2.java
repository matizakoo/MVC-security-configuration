package auth.rest.security3.service;

import auth.rest.security3.domain.Person;

import java.util.List;

public interface Service2 {
    String create(Person person);
    List<Person> findAll();
}
