package dev.paritosh.hellosecurity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/admin/setting")
    public String setting(){
        return "setting";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "accessDenied";
    }
}
