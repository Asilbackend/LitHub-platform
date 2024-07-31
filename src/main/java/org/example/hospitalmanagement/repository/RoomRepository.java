package org.example.hospitalmanagement.repository;

import org.example.hospitalmanagement.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}