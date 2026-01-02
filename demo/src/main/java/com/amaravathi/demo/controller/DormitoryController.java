package com.amaravathi.demo.controller;

import com.amaravathi.demo.model.Dormitory;
import com.amaravathi.demo.service.DormitoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dormitories")
public class DormitoryController {

    @Autowired
    private DormitoryService dormitoryService;

    // Get all dormitory beds
    @GetMapping
    public List<Dormitory> getAllBeds() {
        return dormitoryService.getAllBeds();
    }

    // Get bed by ID
    @GetMapping("/{id}")
    public Dormitory getBedById(@PathVariable Long id) {
        return dormitoryService.getBedById(id);
    }

    // Get beds by type (AC/NON-AC)
    @GetMapping("/type/{type}")
    public List<Dormitory> getBedsByType(@PathVariable String type) {
        return dormitoryService.getBedsByType(type);
    }

    // Get available beds
    @GetMapping("/available")
    public List<Dormitory> getAvailableBeds() {
        return dormitoryService.getAvailableBeds();
    }

    // Get available beds by type
    @GetMapping("/available/{type}")
    public List<Dormitory> getAvailableBedsByType(@PathVariable String type) {
        return dormitoryService.getAvailableBedsByType(type);
    }

    // Create a new bed (admin only later)
    @PostMapping("/create")
    public Dormitory createBed(@RequestBody Dormitory dormitory) {
        return dormitoryService.createBed(
                dormitory.getBedNumber(),
                dormitory.getType(),
                dormitory.getPrice(),
                dormitory.getDescription()
        );
    }
}