package auth.rest.security3.repository;


import auth.rest.security3.domain.Roles;
import auth.rest.security3.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);

    List<Users> findAllByRole(Roles role);
}
