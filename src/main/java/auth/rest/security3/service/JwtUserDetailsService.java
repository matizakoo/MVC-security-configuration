package auth.rest.security3.service;

import auth.rest.security3.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Log4j2
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub

//        String response= productServiceRepository.fetchPasswordForUserName(username);
        String response = usersRepository.findByUsername(username).getPassword();

        if (!response.equals("")) {
            return new User(username, response, new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}