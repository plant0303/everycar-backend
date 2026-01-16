package com.road_friends.everycar.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "아이디 또는 비밀번호가 틀렸습니다.");
        }
        return "admin/login";
    }

//    @GetMapping("/dashboard")
//    public String dashboard(Model model, Principal principal) {
//        // Principal을 통해 세션에 저장된 현재 로그인 아이디를 가져올 수 있음
//        model.addAttribute("adminId", principal.getName());
//        return "admin/user/list";
//    }
}