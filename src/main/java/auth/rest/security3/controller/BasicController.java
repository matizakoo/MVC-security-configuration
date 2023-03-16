package auth.rest.security3.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
public class BasicController {
    @GetMapping("/hi")
    public String hi(Principal principal){
        return "hi " + principal.getName();
    }

    @GetMapping("/home")
    public String home() {
        return  BookController.url + "/dto ,/{id} " + "<br>" +
                UsersController.url + "/findby/{role}" + "<br>" +
                AuthorController.url + " /dto, /nodto" + "<br>" +
                "<a href=\"http://localhost:8080/swagger-ui/index.html#/\">swagger</a>";
    }
}
