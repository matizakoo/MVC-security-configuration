package auth.rest.security3.service;

import auth.rest.security3.domain.Roles;
import auth.rest.security3.domain.Users;
import auth.rest.security3.dto.UsersDTO;
import auth.rest.security3.mapper.UsersMapper;
import auth.rest.security3.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersMapper usersMapper;
    private final UsersRepository usersRepository;
    @Override
    public List<UsersDTO> findAllByRole(Roles role) {
        return usersRepository.findAllByRole(role)
                .stream()
                .map(users -> {
                    UsersDTO dto = usersMapper.usersToUsersDto(users);
                    dto.setPassword(null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void saveAll(List<Users> users) {
        usersRepository.saveAll(users);
    }

    @Override
    public List<UsersDTO> findAll() {
        return usersRepository.findAll()
                .stream()
                .map(users -> {
                    UsersDTO dto = usersMapper.usersToUsersDto(users);
                    dto.setPassword(null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Users save(Users users) {
        return usersRepository.save(users);
    }

    @Override
    public Optional<Users> findById(Integer id) {
        return usersRepository.findById(id);
    }

    @Override
    public Optional<Users> findByIdForTwoFA(Integer id, String twofa) {
        Users users = usersRepository.findById(id).get();
        users.setTwofa(twofa);
        return Optional.of(users);
    }

    @Override
    public Optional<Users> findByUsername(String username) {
        return Optional.of(usersRepository.findByUsername(username));
    }
}
