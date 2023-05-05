package auth.rest.security3.controller;

import auth.rest.security3.domain.Person;
import auth.rest.security3.domain.PersonVO;
import auth.rest.security3.service.PersonServiceImpl;
import auth.rest.security3.service.Service2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = LdapController.url)
public class LdapController {
    public static final String url = "/ldap";

    @Autowired
    private PersonServiceImpl personService;
    @Autowired
    private Service2 service2;

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
        return personService.find(uid);
    }

    @PostMapping(value = "/create")
    public String create(@RequestBody PersonVO personVO) {
        System.out.println("controller");
        personService.create(personVO);
        return "zreloaduj entries w tym chujowym apaczu";
    }

//    @DeleteMapping(value = "/delete")
//    public String delete(@PathVariable String uid) {
//        return personService.delete(uid);
//    }
}
