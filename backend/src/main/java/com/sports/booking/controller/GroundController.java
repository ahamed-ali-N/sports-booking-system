package com.sports.booking.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.sports.booking.entity.Ground;
import com.sports.booking.service.GroundService;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class GroundController {

    private final GroundService groundService;

    public GroundController(GroundService groundService) {
        this.groundService = groundService;
    }

    // Add Ground
    @PostMapping("/grounds")
    public Ground addGround(@Valid @RequestBody Ground ground) {
        return groundService.addGround(ground);
    }

    // Get All Grounds
    @GetMapping("/grounds")
    public List<Ground> getAllGrounds() {
        return groundService.getAllGrounds();
    }

    // Search Ground By Sport
    @GetMapping("/grounds/search")
    public List<Ground> searchBySport(@RequestParam String sport) {
        return groundService.searchBySport(sport);
    }

    // Get Ground By ID
    @GetMapping("/grounds/{id}")
    public Ground getGroundById(@PathVariable Long id) {
        return groundService.getGroundById(id);
    }

    // Update Ground
    @PutMapping("/grounds/{id}")
    public Ground updateGround(
            @PathVariable Long id,
            @Valid @RequestBody Ground ground) {

        return groundService.updateGround(id, ground);
    }

    // Delete Ground
    @DeleteMapping("/grounds/{id}")
    public String deleteGround(@PathVariable Long id) {
        return groundService.deleteGround(id);
    }
}