package com.amaravathi.demo.service;

import com.amaravathi.demo.model.Room;
import com.amaravathi.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    // Get all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Get room by ID
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    // Get rooms by type (AC/NON-AC)
    public List<Room> getRoomsByType(String type) {
        return roomRepository.findByType(type);
    }

    // Get available rooms
    public List<Room> getAvailableRooms() {
        return roomRepository.findByIsAvailable(true);
    }

    // Get available rooms by type
    public List<Room> getAvailableRoomsByType(String type) {
        return roomRepository.findByTypeAndIsAvailable(type, true);
    }

    // Create a new room (for owner/admin)
    public Room createRoom(String roomNumber, String type, Double price, String description) {
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setType(type);
        room.setPrice(price);
        room.setDescription(description);
        room.setIsAvailable(true);

        return roomRepository.save(room);
    }

    // Mark room as unavailable (booked)
    public void markRoomAsBooked(Long roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            room.setIsAvailable(false);
            roomRepository.save(room);
        }
    }

    // Mark room as available (after checkout)
    public void markRoomAsAvailable(Long roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            room.setIsAvailable(true);
            roomRepository.save(room);
        }
    }
}