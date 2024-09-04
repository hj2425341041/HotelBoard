package com.example.hotelboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home() {
        return "home"; // templates/home.html
    }

    @GetMapping("/hotel/a_01")
    public String suiteRoom() {
        return "hotel/a_01";
    }

    @GetMapping("/hotel/a_02")
    public String deluxeRoom() {
        return "hotel/a_02";
    }

    @GetMapping("/hotel/a_03")
    public String standardRoom() {
        return "hotel/a_03";
    }
    @GetMapping("/hotel/b_01")
    public String map() {
        return "hotel/b_01";
    }

    @GetMapping("/hotel/c_01")
    public String park1() {
        return "hotel/c_01";
    }

    @GetMapping("/hotel/c_02")
    public String mountain() {
        return "hotel/c_02";
    }

    @GetMapping("/hotel/c_03")
    public String park2() {
        return "hotel/c_03";
    }

    @GetMapping("/reservation/reserveNew")
    public String reserveNew() {
        return "reservation/reserveNew";
    }
}

