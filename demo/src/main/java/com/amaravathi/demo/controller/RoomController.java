package com.amaravathi.demo.controller;

import com.amaravathi.demo.model.Room;
import com.amaravathi.demo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Get all rooms
    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    // Get room by ID
    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    // Get rooms by type (AC/NON-AC)
    @GetMapping("/type/{type}")
    public List<Room> getRoomsByType(@PathVariable String type) {
        return roomService.getRoomsByType(type);
    }

    // Get available rooms
    @GetMapping("/available")
    public List<Room> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }

    // Get available rooms by type
    @GetMapping("/available/{type}")
    public List<Room> getAvailableRoomsByType(@PathVariable String type) {
        return roomService.getAvailableRoomsByType(type);
    }

    // Create a new room (admin only later)
    @PostMapping("/create")
    public Room createRoom(@RequestBody Room room) {
        return roomService.createRoom(
                room.getRoomNumber(),
                room.getType(),
                room.getPrice(),
                room.getDescription()
        );
    }
}