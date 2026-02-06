package com.projet.controller;

import com.framework.annotation.RestController;
import com.framework.annotation.GetMapping;
import com.framework.annotation.PostMapping;
import com.framework.annotation.PutMapping;
import com.framework.annotation.DeleteMapping;
import com.framework.annotation.PathVariable;
import com.framework.annotation.RequestBody;
import com.framework.annotation.CrossOrigin;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

import com.projet.model.Hotel;
import com.projet.repository.HotelRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class HotelController {

    private HotelRepository hotelRepository = new HotelRepository();

    /**
     * GET /api/hotels - Liste tous les hôtels
     */
    @GetMapping("/api/hotels")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        try {
            List<Hotel> hotels = hotelRepository.findAll();
            return ResponseEntity.ok(hotels);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/hotels/{id} - Récupère un hôtel par ID
     */
    @GetMapping("/api/hotels/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable("id") int id) {
        try {
            Hotel hotel = hotelRepository.findById(id);
            if (hotel != null) {
                return ResponseEntity.ok(hotel);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * POST /api/hotels - Crée un nouvel hôtel
     */
    @PostMapping("/api/hotels")
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        try {
            Hotel created = hotelRepository.save(hotel);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * PUT /api/hotels/{id} - Met à jour un hôtel
     */
    @PutMapping("/api/hotels/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable("id") int id, @RequestBody Hotel hotel) {
        try {
            hotel.setIdHotel(id);
            Hotel updated = hotelRepository.update(hotel);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * DELETE /api/hotels/{id} - Supprime un hôtel
     */
    @DeleteMapping("/api/hotels/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable("id") int id) {
        try {
            hotelRepository.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
