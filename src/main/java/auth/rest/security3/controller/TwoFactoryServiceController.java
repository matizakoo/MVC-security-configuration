package auth.rest.security3.controller;

import auth.rest.security3.config.twofa.SMSServiceImpl;
import auth.rest.security3.domain.Users;
import auth.rest.security3.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class TwoFactoryServiceController {
    @Autowired
    private SMSServiceImpl smsServiceImpl;
    @Autowired
    private UsersService usersService;

    @GetMapping(value = "/user/twofa/{userid}/mobilenumbers/{mobilenumber}")
    public ResponseEntity<Object> send2faCode(@PathVariable("userid") String id, @PathVariable("mobilenumber") String mobile) {
        String twofa = String.valueOf(new Random().nextInt(9999) + 1000);
        Users users = usersService.findByIdForTwoFA(Integer.valueOf(id), twofa).get();
        smsServiceImpl.send2FACode(users.getNumber(), twofa);
        usersService.save(users);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
