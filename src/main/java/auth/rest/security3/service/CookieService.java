package auth.rest.security3.service;


import jakarta.servlet.http.Cookie;

public class CookieService extends Cookie {
    private boolean bol;
    private boolean isSecure;
    private String path;
    private int age;
    public CookieService(String name, String value) {
        super(name, value);
    }

    public CookieService(String name, String value, boolean bol, boolean isSecure, String path, int age){
        super(name, value);
        this.bol = bol;
        this.path = path;
        this.isSecure = isSecure;
        this.age = age;
    }
}
