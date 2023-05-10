package auth.rest.security3.service;

import auth.rest.security3.domain.People;
import auth.rest.security3.domain.Person;
import auth.rest.security3.dto.PersonVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

public interface PersonService {
    void create(PersonVO p);
    List<Person> findAll();
    Person findByUid(String uid);
    Person findByUsername(String username);
    Person findByUsernameWithCorrectCredentials(String username, String password);
    People findByCn(String cn);
    List<People> all();
    People findByEmail(String email);
}
