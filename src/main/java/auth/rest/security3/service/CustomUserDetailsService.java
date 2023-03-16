package auth.rest.security3.service;

import auth.rest.security3.domain.Users;
import auth.rest.security3.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findByUsername(username);
        if(users == null)
            throw new UsernameNotFoundException("Username not found");
        return Users.builder()
                .username(users.getUsername())
                .password(users.getPassword())
                .role(users.getRole())
                .devices(new HashSet<>())
                .build();
    }
}
