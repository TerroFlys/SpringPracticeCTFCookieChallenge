package be.chenko.springpractice.controllers;

import be.chenko.springpractice.models.Cookie;
import be.chenko.springpractice.repositories.CookieRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger("Main Logger");

    @Autowired
    CookieRepository cookieRepository;
    @GetMapping({"/", "/home", })
    public String homePage(@CookieValue(name = "value", defaultValue = "0") String cookieCookie,
                           Model model){
        int value = 0;
        try {
            value = Integer.parseInt(cookieCookie);
            logger.info("parsed int");
        } catch (NumberFormatException e) {
            logger.info("catched error" + e);
            value = -1;
        }
        Optional<Cookie> optCookie = cookieRepository.findById(value);
        logger.info("created optionalCookie");
        if (value == 0){
            logger.info("Value was 0");
        }
        else if (optCookie.isPresent()){
            if (optCookie.get().getId() == 15){
                model.addAttribute("flag", optCookie.get());
                logger.info("flag is present");
            }else{
                model.addAttribute("cookie", optCookie.get());
                logger.info("cookie is present");
            }

        } else {
            model.addAttribute("wrong", true);
            logger.info("Cookie not present");
        }
        logger.info("load home");
        return "home";
    }

    @PostMapping("/check")
    public String check(@RequestParam(required = true) String name, Model model, HttpServletResponse response){
        Optional<Cookie> optCookie = cookieRepository.findByNameAllIgnoreCase(name);
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("value", "0");


        if (optCookie.isPresent()){
            cookie.setValue(optCookie.get().getId()+"");
        } else {
            cookie.setValue("-1");
        }
        response.addCookie(cookie);
        return "redirect:/home";
    }

    @GetMapping("/reset")
    public String reset(HttpServletResponse response){
        //remove cookie info
        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("value", "0");

        response.addCookie(cookie);
        return "redirect:/home";
    }
}
