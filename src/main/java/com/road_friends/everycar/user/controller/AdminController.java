package com.road_friends.everycar.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 있으면 가져오고 없으면 null
        if (session != null) {
            session.invalidate(); // 세션 무효화 (모든 데이터 삭제)
        }
        return "redirect:/admin/login"; // 로그인 페이지로 리다이렉트
    }

//    @GetMapping("/dashboard")
//    public String dashboard(Model model, Principal principal) {
//        // Principal을 통해 세션에 저장된 현재 로그인 아이디를 가져올 수 있음
//        model.addAttribute("adminId", principal.getName());
//        return "admin/user/list";
//    }
}