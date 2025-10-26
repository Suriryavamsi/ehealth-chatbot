package org.ehealth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/ehealth-chatbot/")
    public String index() {
        return "index"; // maps to WEB-INF/views/index.jsp
    }
}
