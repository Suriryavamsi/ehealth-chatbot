package org.ehealth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/doctor/dashboard")
    public String doctorDashboard() {
        return "doctor/dashboard"; // renders templates/doctor/dashboard.html
    }

    @GetMapping("/patient/dashboard")
    public String patientDashboard() {
        return "patient/dashboard"; // renders templates/patient/dashboard.html
    }

    @GetMapping("/nurse/dashboard")
    public String nurseDashboard() {
        return "nurse/dashboard"; // renders templates/nurse/dashboard.html
    }

    @GetMapping("/")
    public String index() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Signup page
    @GetMapping("/signup")
    public String signup() {
        return "signup"; // renders templates/signup.html
    }

    // Logout action
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // invalidate the session
        }
        return "redirect:/"; // redirect to login page
    }
}
