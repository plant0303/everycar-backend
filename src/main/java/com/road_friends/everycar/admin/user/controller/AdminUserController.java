package com.road_friends.everycar.admin.user.controller;

import com.road_friends.everycar.admin.user.service.AdminUserService;
import com.road_friends.everycar.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    // 회원 목록 페이지
    @GetMapping
    public String list(@RequestParam(value = "page", defaultValue = "1") int page,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       Model model) {
        Map<String, Object> result = adminUserService.getUserList(keyword, page);
        model.addAllAttributes(result);
        return "admin/user/list";
    }
    // 회원 수정 폼 페이지
    @GetMapping("/edit/{userNum}")
    public String editForm(@PathVariable Long userNum, Model model) {
        model.addAttribute("user", adminUserService.getUserByNum(userNum));
        model.addAttribute("reservations", adminUserService.getReservationsByUser(userNum));
        return "admin/user/edit";
    }

    // 회원 수정 처리
    @PostMapping("/edit")
    public String edit(@ModelAttribute UserDTO userDTO, RedirectAttributes re) {
        adminUserService.updateUser(userDTO);
        re.addFlashAttribute("msg", "회원 정보가 수정되었습니다.");
        return "redirect:/admin/users";
    }
}