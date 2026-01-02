package com.amaravathi.demo.repository;

import com.amaravathi.demo.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByType(String type); // Find rooms by AC/NON-AC
    List<Room> findByIsAvailable(Boolean isAvailable); // Find available rooms
    List<Room> findByTypeAndIsAvailable(String type, Boolean isAvailable); // Filter both
    Room findByRoomNumber(String roomNumber); // Find by room number
}