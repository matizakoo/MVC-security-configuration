package auth.rest.security3.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonVO {
    private String dn;
    private String fullname;
    private String lastname;
    private String givenname;
    private String description;
    private String mail;
    private String uid;
}
