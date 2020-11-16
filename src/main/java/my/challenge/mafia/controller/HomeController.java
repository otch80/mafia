package my.challenge.mafia.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    //메인 화면
    @GetMapping("/")
    public String home() {
        return "/home/home";
    }

    @PostMapping("/ready")
    public String ready() {
        return "/admin/index";
    }

}
