package fr.stageLIS.long_jump_serveur.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UrlController {

    @GetMapping("/")
    public String redirectToIndex() {
        return "redirect:/static/index.html";
    }
}
