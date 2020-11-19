package my.challenge.mafia.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    //메인 화면
<<<<<<< HEAD
    @GetMapping("/home")
=======
    @GetMapping("/")
>>>>>>> 89eb01b1b390974067a5127f012df23423fa4786
    public String home() {
        return "/home/home";
    }

<<<<<<< HEAD
    @GetMapping("/ttt")
    public String index() {
        return "/admin/index";
    }
=======
    // 임시추가
    @GetMapping("/ready")
    public String ready() {
        return "/home/ready";
    }

>>>>>>> 89eb01b1b390974067a5127f012df23423fa4786
}
