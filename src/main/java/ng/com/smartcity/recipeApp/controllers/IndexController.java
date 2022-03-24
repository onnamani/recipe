package ng.com.smartcity.recipeApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping({"", "/", "/index"})
    public String getIndexPage() {
        System.out.println("run getIndexPage");
        return "index";
    }
}
