package auth.rest.security3.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonVO {
    private String dn;
    private String fullname;
    private String lastname;
    private String password;
    private String description;
    private String mail;
    private String uid;
}
