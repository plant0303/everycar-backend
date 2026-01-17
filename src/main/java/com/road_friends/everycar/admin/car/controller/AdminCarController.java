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

    // 1. 차량 목록 조회 (검색/필터/페이징)
    @GetMapping
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Integer status,
                       Model model) {
        // 검색 조건 유지 및 페이징 데이터 전달
        model.addAllAttributes(carService.getCarList(page, keyword, status));
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        return "admin/car/list";
    }

    // 2. 차량 등록 폼 이동
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("car", new CarAdminDTO());
        // 셀렉트 박스를 위한 모델 목록과 주차장 목록 추가
        model.addAttribute("models", carService.getAllModels());
        model.addAttribute("parkings", carService.getAllParkingLots());
        return "admin/car/register";
    }

    // 3. 차량 등록 실행
    @PostMapping("/register")
    public String register(@ModelAttribute CarAdminDTO dto, RedirectAttributes ra) {
        carService.registerCar(dto);
        ra.addFlashAttribute("msg", "신규 차량이 등록되었습니다.");
        return "redirect:/admin/cars";
    }

    // 4. 차량 수정 폼 이동 (현재 대여 정보 포함 조회)
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("car", carService.getCar(id));
        // 이 차량의 예약 리스트를 추가로 담아서 보냄
        model.addAttribute("reservationList", carService.getCarReservations(id));

        model.addAttribute("models", carService.getAllModels());
        model.addAttribute("parkings", carService.getAllParkingLots());
        return "admin/car/edit";
    }

    // 5. 차량 정보 수정 실행
    @PostMapping("/update")
    public String update(@ModelAttribute CarAdminDTO dto, RedirectAttributes ra) {
        carService.updateCar(dto);
        ra.addFlashAttribute("msg", "차량 정보가 수정되었습니다.");
        return "redirect:/admin/cars";
    }

    // 6. 차량 삭제 실행
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        carService.removeCar(id);
        ra.addFlashAttribute("msg", "차량이 삭제되었습니다.");
        return "redirect:/admin/cars";
    }
}