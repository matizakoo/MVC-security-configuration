package auth.rest.security3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = LdapController.url)
public class LdapController {
    public static final String url = "/ldap";

    @GetMapping("/test")
    public String secureMethod() {
        return "secured!";
    }


}
