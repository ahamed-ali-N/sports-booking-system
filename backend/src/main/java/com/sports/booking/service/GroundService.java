package com.sports.booking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sports.booking.entity.Ground;
import com.sports.booking.Repository.GroundRepository;

@Service
public class GroundService {

    private final GroundRepository groundRepository;

    public GroundService(GroundRepository groundRepository) {
        this.groundRepository = groundRepository;
    }

    // Add Ground
    public Ground addGround(Ground ground) {
        return groundRepository.save(ground);
    }

    // Get All Grounds
    public List<Ground> getAllGrounds() {
        return groundRepository.findAll();
    }

    // Search Ground By Sport
    public List<Ground> searchBySport(String sport) {
        return groundRepository.findBySportIgnoreCase(sport);
    }

    // Get Ground By ID
    public Ground getGroundById(Long id) {
        return groundRepository.findById(id).orElse(null);
    }

    // Update Ground
    public Ground updateGround(Long id, Ground ground) {

        Ground existingGround =
                groundRepository.findById(id).orElse(null);

        if (existingGround != null) {

            existingGround.setName(ground.getName());
            existingGround.setLocation(ground.getLocation());
            existingGround.setSport(ground.getSport());
            existingGround.setPrice(ground.getPrice());

            return groundRepository.save(existingGround);
        }

        return null;
    }

    // Delete Ground
    public String deleteGround(Long id) {

        if (groundRepository.existsById(id)) {
            groundRepository.deleteById(id);
            return "Ground deleted successfully";
        }

        return "Ground not found";
    }
}