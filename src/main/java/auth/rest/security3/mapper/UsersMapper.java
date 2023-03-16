package auth.rest.security3.mapper;


import auth.rest.security3.domain.Users;
import auth.rest.security3.dto.UsersDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface UsersMapper {
    Users usersDtoToUsers(UsersDTO usersDTO);
    UsersDTO usersToUsersDto(Users users);
}
