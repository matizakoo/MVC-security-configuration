package auth.rest.security3.service;


import auth.rest.security3.domain.Roles;
import auth.rest.security3.domain.Users;
import auth.rest.security3.dto.UsersDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersService {
    List<UsersDTO> findAllByRole(Roles role);
    void saveAll(List<Users> users);
    List<UsersDTO> findAll();
}
