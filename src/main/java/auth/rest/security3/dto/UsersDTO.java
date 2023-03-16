package auth.rest.security3.dto;


import auth.rest.security3.domain.Devices;
import auth.rest.security3.domain.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {
    private Integer id;
    private String username;
    private String password;
    private Roles role;
    private Set<Devices> devices;
}
