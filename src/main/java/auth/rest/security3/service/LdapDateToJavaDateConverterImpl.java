package auth.rest.security3.service;

import auth.rest.security3.mapper.StringUtils;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static auth.rest.security3.service.LdapDateToJavaDateConverter.LDAP_DATE_FORMAT;

@Service
public class LdapDateToJavaDateConverterImpl implements LdapDateToJavaDateConverter {
    private DateTimeFormatter formatter;

    public LdapDateToJavaDateConverterImpl() {
        formatter = DateTimeFormatter.ofPattern(LDAP_DATE_FORMAT);
    }

    public String toSource(ZonedDateTime destination) {
        if (destination == null) {
            return null;
        }

        return destination.format(formatter);
    }

    public ZonedDateTime toDestination(String source) {
        if (StringUtils.isNullOrEmpty(source)) {
            return null;
        }

        return ZonedDateTime.parse(source, formatter);
    }
}
