package auth.rest.security3.service;

import auth.rest.security3.domain.Person;
import auth.rest.security3.domain.PersonVO;

import java.util.List;

public interface PersonService {
    void create(PersonVO p);
    List<Person> findAll();
//    String delete(String uid);

    Person find(String uid);
}
