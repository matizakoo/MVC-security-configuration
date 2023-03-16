package auth.rest.security3.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Devices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;
}
