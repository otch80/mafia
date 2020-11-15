package my.challenge.mafia.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    //메인 화면
    @GetMapping("/home")
    public String home() {
        return "/home/home";
    }

    @GetMapping("/ttt")
    public String index() {
        return "/admin/index";
    }
}
