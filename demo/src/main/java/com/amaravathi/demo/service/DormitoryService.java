package com.amaravathi.demo.service;

import com.amaravathi.demo.model.Dormitory;
import com.amaravathi.demo.repository.DormitoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DormitoryService {

    @Autowired
    private DormitoryRepository dormitoryRepository;

    // Get all dormitory beds
    public List<Dormitory> getAllBeds() {
        return dormitoryRepository.findAll();
    }

    // Get bed by ID
    public Dormitory getBedById(Long id) {
        return dormitoryRepository.findById(id).orElse(null);
    }

    // Get beds by type (AC/NON-AC)
    public List<Dormitory> getBedsByType(String type) {
        return dormitoryRepository.findByType(type);
    }

    // Get available beds
    public List<Dormitory> getAvailableBeds() {
        return dormitoryRepository.findByIsAvailable(true);
    }

    // Get available beds by type
    public List<Dormitory> getAvailableBedsByType(String type) {
        return dormitoryRepository.findByTypeAndIsAvailable(type, true);
    }

    // Create a new bed (for owner/admin)
    public Dormitory createBed(String bedNumber, String type, Double price, String description) {
        Dormitory bed = new Dormitory();
        bed.setBedNumber(bedNumber);
        bed.setType(type);
        bed.setPrice(price);
        bed.setDescription(description);
        bed.setIsAvailable(true);

        return dormitoryRepository.save(bed);
    }

    // Mark bed as unavailable (booked)
    public void markBedAsBooked(Long bedId) {
        Dormitory bed = dormitoryRepository.findById(bedId).orElse(null);
        if (bed != null) {
            bed.setIsAvailable(false);
            dormitoryRepository.save(bed);
        }
    }

    // Mark bed as available (after checkout)
    public void markBedAsAvailable(Long bedId) {
        Dormitory bed = dormitoryRepository.findById(bedId).orElse(null);
        if (bed != null) {
            bed.setIsAvailable(true);
            dormitoryRepository.save(bed);
        }
    }
}