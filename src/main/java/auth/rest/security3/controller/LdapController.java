package auth.rest.security3.controller;

import auth.rest.security3.config.jwt.JwtGenerator;
import auth.rest.security3.config.jwt.JwtGeneratorLdap;
import auth.rest.security3.domain.Person;
import auth.rest.security3.dto.PersonVO;
import auth.rest.security3.dto.UserCredentialsDTO;
import auth.rest.security3.service.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = LdapController.url)
public class LdapController {
    public static final String url = "/ldap";
    @Autowired
    private JwtGeneratorLdap jwtGeneratorLdap;
    @Autowired
    private PersonServiceImpl personService;

    @GetMapping("/test")
    public String secureMethod() {
        return "secured!";
    }

    @GetMapping(value = "/get")
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping(value = "/find/{uid}")
    public Person find(@PathVariable String uid) {
        return personService.findByUid(uid);
    }

    @PostMapping(value = "/create")
    public String create(@RequestBody PersonVO personVO) {
        personService.create(personVO);
        return "zreloaduj entries w apaczu";
    }

    @PostMapping("/auth")
    public String token(@RequestBody UserCredentialsDTO userCredentialsDTO) {
        Person person = personService.findByUsernameWithCorrectCredentials(
                userCredentialsDTO.getUsername(), userCredentialsDTO.getPassword());
        return jwtGeneratorLdap.generateTokenLdap(person);
    }

    @PostMapping("/valid/{token}")
    public boolean isValid(@PathVariable("token") String token) {
        return jwtGeneratorLdap.validateTokenLdap(token);
    }
}
