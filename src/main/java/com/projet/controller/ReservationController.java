package com.projet.controller;

import com.framework.annotation.RestController;
import com.framework.annotation.GetMapping;
import com.framework.annotation.PostMapping;
import com.framework.annotation.PutMapping;
import com.framework.annotation.DeleteMapping;
import com.framework.annotation.PathVariable;
import com.framework.annotation.RequestParam;
import com.framework.annotation.RequestBody;
import com.framework.annotation.CrossOrigin;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

import com.projet.model.Reservation;
import com.projet.repository.ReservationRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ReservationController {

    private ReservationRepository reservationRepository = new ReservationRepository();

    /**
     * GET /api/reservations - Liste toutes les réservations
     */
    @GetMapping("/api/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/reservations/date/{date} - Filtre les réservations par date
     * Format date: YYYY-MM-DD
     */
    @GetMapping("/api/reservations/date/{date}")
    public ResponseEntity<List<Reservation>> getReservationsByDate(@PathVariable("date") String date) {
        try {
            List<Reservation> reservations = reservationRepository.findByDate(date);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/reservations/{id} - Récupère une réservation par ID
     */
    @GetMapping("/api/reservations/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id") int id) {
        try {
            Reservation reservation = reservationRepository.findById(id);
            if (reservation != null) {
                return ResponseEntity.ok(reservation);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * POST /api/reservations - Crée une nouvelle réservation
     * Body: { "idClient": 1, "nbPassager": 3, "dateHeureArrivee": "2026-02-06 14:30:00", "idHotel": 1 }
     */
    @PostMapping("/api/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        try {
            Reservation created = reservationRepository.save(reservation);
            // Recharger pour avoir les noms
            created = reservationRepository.findById(created.getIdReservation());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * PUT /api/reservations/{id} - Met à jour une réservation
     */
    @PutMapping("/api/reservations/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable("id") int id, 
            @RequestBody Reservation reservation) {
        try {
            reservation.setIdReservation(id);
            Reservation updated = reservationRepository.update(reservation);
            // Recharger pour avoir les noms
            updated = reservationRepository.findById(id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * DELETE /api/reservations/{id} - Supprime une réservation
     */
    @DeleteMapping("/api/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") int id) {
        try {
            reservationRepository.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
