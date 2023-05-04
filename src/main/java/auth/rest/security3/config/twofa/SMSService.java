package auth.rest.security3.config.twofa;

public interface SMSService {
    boolean send2FACode(String mobilenumber, String twoFaCode);
}
