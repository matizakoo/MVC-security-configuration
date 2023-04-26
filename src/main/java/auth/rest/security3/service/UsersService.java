package auth.rest.security3.service;


import auth.rest.security3.domain.Roles;
import auth.rest.security3.domain.Users;
import auth.rest.security3.dto.UsersDTO;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UsersService {
    List<UsersDTO> findAllByRole(Roles role);
    void saveAll(List<Users> users);
    List<UsersDTO> findAll();
    Users save(Users users);
    Optional<Users> findById(Integer id);
    Optional<Users> findByIdForTwoFA(Integer id, String twofa);
    Optional<Users> findByUsername(String username);
}
