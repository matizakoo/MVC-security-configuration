package auth.rest.security3.config.twofa;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class SMSService {
    private final static String ACCOUNT_SID = "ACe83ea441425322750b69802c93719438";
    private final static String AUTH_ID = "52f64c988028114b0e12e5974ec49ea9";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_ID);
    }
    public boolean send2FACode(String mobilenumber, String twoFaCode) {
        Message.creator(new PhoneNumber(mobilenumber), new PhoneNumber("+16812026994"),
        "Hi, your 2FA code: " + twoFaCode).create();
        return true;
    }
}
