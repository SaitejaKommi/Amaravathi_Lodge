package com.amaravathi.demo.repository;

import com.amaravathi.demo.model.Dormitory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DormitoryRepository extends JpaRepository<Dormitory, Long> {
    List<Dormitory> findByType(String type); // Find beds by AC/NON-AC
    List<Dormitory> findByIsAvailable(Boolean isAvailable); // Find available beds
    List<Dormitory> findByTypeAndIsAvailable(String type, Boolean isAvailable); // Filter both
    Dormitory findByBedNumber(String bedNumber); // Find by bed number
}