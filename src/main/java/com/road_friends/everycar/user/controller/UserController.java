package com.road_friends.everycar.user.controller;

import com.road_friends.everycar.user.dto.UserDTO;
import com.road_friends.everycar.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        List<UserDTO> users = userService.getAllUsers();

        // ✅ 여기서 로그 출력
        for (UserDTO user : users) {
            System.out.println("user.userNum = " + user.getUserNum());
        }

        model.addAttribute("users", users);
        return "users";
    }
}