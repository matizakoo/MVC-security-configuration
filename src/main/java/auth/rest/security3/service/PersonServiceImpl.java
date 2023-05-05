package auth.rest.security3.service;

import auth.rest.security3.domain.Person;
import auth.rest.security3.domain.PersonVO;
import auth.rest.security3.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class PersonServiceImpl implements PersonService{
    @Autowired
    private LdapTemplate ldapTemplate;

    @Override
    public void create(PersonVO p) {
        System.out.println("1");
        Person person = new Person();
        person.setDn(LdapNameBuilder.newInstance(p.getDn()).build());
        System.out.println("2");
        person.setFullname(p.getFullname());
        person.setLastname(p.getLastname());
        person.setDescription(p.getDescription());
        person.setGivenname(p.getGivenname());
        person.setMail(p.getMail());
        person.setUid(p.getUid());
        System.out.println("3");
        ldapTemplate.create(person);
        System.out.println("4");
    }

    @Override
    public List<Person> findAll() {
        return ldapTemplate.find(query().base("ou=people"), Person.class);
    }

    @Override
    public Person find(String uid) {
        return ldapTemplate.findOne(query().base("ou=people").where("uid").is(uid), Person.class);
    }


//    @Override
//    public String delete(String uid) {
//        Person p = personRepository.findOne(LdapQueryBuilder.query().where("uid").is(uid)).get();
//        personRepository.delete(p);
//        return "delete";
//    }
}
