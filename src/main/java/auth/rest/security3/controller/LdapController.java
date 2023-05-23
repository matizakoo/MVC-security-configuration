package auth.rest.security3.controller;

import auth.rest.security3.config.ldap.JwtGeneratorLdap;
import auth.rest.security3.domain.People;
import auth.rest.security3.domain.Person;
import auth.rest.security3.dto.PersonVO;
import auth.rest.security3.dto.UserCredentialsDTO;
import auth.rest.security3.service.LdapDateToJavaDateConverterImpl;
import auth.rest.security3.service.PersonServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = LdapController.url)
public class LdapController {
    public static final String url = "/ldap";
    @Autowired
    private JwtGeneratorLdap jwtGeneratorLdap;
    @Autowired
    private PersonServiceImpl personService;
    @Autowired
    private LdapDateToJavaDateConverterImpl ldapDateToJavaDateConverter;

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

    @PostMapping(value = "/auth")
    public String token(@RequestBody UserCredentialsDTO userCredentialsDTO) {
        Person person = personService.findByUsernameWithCorrectCredentials(
                userCredentialsDTO.getUsername(), userCredentialsDTO.getPassword());
        return jwtGeneratorLdap.generateTokenLdap(person);
    }

    @PostMapping(value = "/login")
    public String token2(@RequestBody UserCredentialsDTO userCredentialsDTO) {
        People people = personService.findPeopleByUsernameWuthCorrectCredentials(
                userCredentialsDTO.getUsername(), userCredentialsDTO.getPassword());
        return jwtGeneratorLdap.tokenLdap(people);
    }

    @PostMapping(value = "/valid/{token}")
    public boolean isValid(@PathVariable("token") String token) {
        return jwtGeneratorLdap.validateTokenLdap(token);
    }

    @GetMapping(value = "/findby/{cn}")
    public String existing(@PathVariable("cn") String cn) {
        System.out.println(cn);
        People people = personService.findByCn(cn);
        System.out.println(new String(people.getBusinessId()));
        return people.toString();
    }

    @GetMapping(value = "/all")
    public List<People> all() {
        List<People> ppl = personService.all();
        System.out.println(ppl.size());
        return ppl;
    }

    @GetMapping(value = "/jwt/{cn}")
    public String jwt(@PathVariable("cn") String cn, HttpServletRequest request) {
        System.out.println(request.getParameter("username"));
        return jwtGeneratorLdap.tokenLdap(personService.findByCn(cn));
    }

    @PostMapping(value = "/jwt")
    public String jwtxD(@RequestBody UserCredentialsDTO userCredentialsDTO) {
        System.out.println(userCredentialsDTO.getPassword());
        return "xD";
    }


    @GetMapping(value = "/user")
    public String user() {
        return "user";
    }

    @GetMapping(value = "/admin")
    public String admin() {
        DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss[.SSSX]");
        System.out.println(ZonedDateTime.parse("20230316083338.404+01", formatter));
        return "admin";
    }

    @GetMapping(value = "/findbyemail/{email}")
    public String findByEmail(@PathVariable("email") String email) {
        return personService.findByEmail(email).getEmailContext();
    }
}
