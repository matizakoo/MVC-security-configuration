package auth.rest.security3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = HttpsController.url)
@RestController
public class HttpsController {
    public static final String url = "/https";

    @GetMapping("/test")
    public String test() {
        return "This is https";
    }
}
