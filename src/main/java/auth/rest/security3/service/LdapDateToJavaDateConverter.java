package auth.rest.security3.service;

import io.netty.handler.codec.ValueConverter;

import java.time.ZonedDateTime;

public interface LdapDateToJavaDateConverter {
    String LDAP_DATE_FORMAT = "yyyyMMddHHmmss[.SSSX]";
}
