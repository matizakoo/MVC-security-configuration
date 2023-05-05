package auth.rest.security3.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Entry(base = "ou=users",
        objectClasses = { "inetOrgPerson","organizationalPerson","person", "top" })
@Getter
@Setter
public final class Person implements Persistable<Name> {
    @Id
    private Name dn;
    @Attribute(name="cn")
    private String fullname;
    @Attribute(name="sn")
    private String lastname;
    @Attribute(name="givenname")
    private String givenname;
    @Attribute(name="description")
    private String description;
    @Attribute(name="mail")
    private String mail;
    @Attribute(name="uid")
    private String uid;

    @Override
    public Name getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
