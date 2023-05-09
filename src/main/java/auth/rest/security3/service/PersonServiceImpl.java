package auth.rest.security3.service;

import auth.rest.security3.domain.Person;
import auth.rest.security3.dto.PersonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class PersonServiceImpl implements PersonService{
    @Autowired
    private LdapTemplate ldapTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void create(PersonVO p) {
        System.out.println("1");
        Person person = new Person();
        person.setDn(LdapNameBuilder.newInstance(p.getDn()).build());
        System.out.println("2");
        person.setFullname(p.getFullname());
        person.setLastname(p.getLastname());
        person.setDescription(p.getDescription());
        person.setPasswordEncoded(p.getPassword());
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
    public Person findByUid(String uid) {
        return ldapTemplate.findOne(query().base("ou=people").where("uid").is(uid), Person.class);
    }

    @Override
    public Person findByUsername(String username) {
        Person person = ldapTemplate.findOne(query().base("ou=people").where("cn").is(username), Person.class);
        return person;
    }

    public Person findByUsernameWithCorrectCredentials(String username, String password) {
        Person person = findByUsername(username);
        if(person == null)
            return null;

        if(new LdapShaPasswordEncoder().matches(password, new String(person.getPassword())))
            return person;

        return null;
    }
}
