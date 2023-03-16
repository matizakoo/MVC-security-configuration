package auth.rest.security3.controller;

import auth.rest.security3.domain.Roles;
import auth.rest.security3.dto.UsersDTO;
import auth.rest.security3.service.UsersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(UsersController.url)
@Slf4j
@SecurityRequirement(name = "sec3")
public class UsersController {
    public static final String url = "/user/users";
    private final UsersService usersService;
    @GetMapping("/findby/{role}")
    public List<UsersDTO> getUsersByRole(@PathVariable("role") String role){
        log.info("ROLE: " + role);
        return usersService.findAllByRole(Roles.valueOf(role.toUpperCase()));
    }

    @GetMapping("/loginpage")
    public String loginPage(){
        System.out.println("/xD");
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\"/>\n" +
                "    <title>Login</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "hi helo\n" +
                "<form action=\"/user/users/auth\" method=\"post\">\n" +
                "    <label for=\"username\">Username:</label>\n" +
                "    <input type=\"text\" id=\"username\" name=\"username\" required=\"required\" autofocus=\"autofocus\" />\n" +
                "    <br/>\n" +
                "    <label for=\"password\">Password:</label>\n" +
                "    <input type=\"password\" id=\"password\" name=\"password\" required=\"required\" />\n" +
                "    <br/>\n" +
                "    <button type=\"submit\">srititti</button>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>";
    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestParam String username, @RequestParam String password){
        System.out.println("aaaaa" + username + " " + password);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/findall")
    public List<UsersDTO> findAll(){
        return usersService.findAll();
    }

    @GetMapping("/user")
    public String user(){
        return "user";
    }

    @GetMapping("/test")
    public String userTest(){
        return "user";
    }

}
