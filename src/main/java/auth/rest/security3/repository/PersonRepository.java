package auth.rest.security3.repository;

import auth.rest.security3.domain.Person;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends LdapRepository<Person> {

    List<Person> findAll();
}
