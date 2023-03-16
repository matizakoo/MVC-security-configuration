package auth.rest.security3.dto;

import auth.rest.security3.domain.DeviceType;
import auth.rest.security3.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    private Integer id;
    private String name;
    private DeviceType deviceType;
    private UUID uuid;
    private Users users;
}
