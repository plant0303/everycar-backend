package com.road_friends.everycar.user.controller;

import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class userController {
    //Get
    @GetMapping("/users")
    public String getItem(Model model){
        model.addAttribute("item");
        return "user";
    }
}
