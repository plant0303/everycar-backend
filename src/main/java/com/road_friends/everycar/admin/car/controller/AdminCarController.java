package com.road_friends.everycar.admin.car.controller;

import com.road_friends.everycar.admin.car.dto.CarAdminDTO;
import com.road_friends.everycar.admin.car.service.AdminCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/cars")
@RequiredArgsConstructor
public class AdminCarController {
    private final AdminCarService carService;

    // 차량 목록 조회 (검색/필터/페이징)
    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Integer status,
                       Model model) {
        model.addAllAttributes(carService.getCarList(page, keyword, status));
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        return "admin/car/list";
    }

    // 차량 등록 폼 이동
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("car", new CarAdminDTO());
        model.addAttribute("models", carService.getAllModels());
        model.addAttribute("parkings", carService.getAllParkingLots());
        return "admin/car/register";
    }

    // 차량 등록 실행
    @PostMapping("/register")
    public String register(@ModelAttribute CarAdminDTO dto, RedirectAttributes ra) {
        try {
            carService.registerCar(dto);

            ra.addFlashAttribute("msg", "차량이 성공적으로 등록되었습니다.");
            return "redirect:/admin/cars";

        } catch (Exception e) {
            // 예외 발생 시 등록 폼으로 이동
            ra.addFlashAttribute("error", "데이터 저장 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/admin/cars/register";
        }
    }

    // 차량 수정 폼 이동
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("car", carService.getCar(id));
        model.addAttribute("reservationList", carService.getCarReservations(id));

        model.addAttribute("models", carService.getAllModels());
        model.addAttribute("parkings", carService.getAllParkingLots());
        return "admin/car/edit";
    }

    // 차량 정보 수정 실행
    @PostMapping("/update")
    public String update(@ModelAttribute CarAdminDTO dto, RedirectAttributes ra) {
        carService.updateCar(dto);

        String msg = String.format("[%s %s] 차량 정보가 성공적으로 수정되었습니다.",
                dto.getModelBrand(), dto.getModelName());

        ra.addFlashAttribute("msg", "차량 정보가 수정되었습니다.");
        return "redirect:/admin/cars";
    }

    //  차량 삭제 실행
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        carService.removeCar(id);
        ra.addFlashAttribute("msg", "차량이 삭제되었습니다.");
        return "redirect:/admin/cars";
    }
}