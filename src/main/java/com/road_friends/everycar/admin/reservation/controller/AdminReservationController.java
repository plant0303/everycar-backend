package com.road_friends.everycar.admin.reservation.controller;

import com.road_friends.everycar.admin.reservation.dto.ReservationDTO;
import com.road_friends.everycar.admin.reservation.service.AdminReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/reservations")
@RequiredArgsConstructor
public class AdminReservationController {
    private final AdminReservationService reservationService;

    @GetMapping
    public String list(@RequestParam(value="page", defaultValue="1") int page, Model model) {
        model.addAllAttributes(reservationService.getReservationList(page));
        return "admin/reservation/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("res", reservationService.getReservation(id));
        return "admin/reservation/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute ReservationDTO dto, RedirectAttributes ra) {
        reservationService.update(dto);
        ra.addFlashAttribute("msg", "수정되었습니다.");
        return "redirect:/admin/reservations";
    }
}