package com.projet.controller;

import com.framework.annotation.*;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

import com.projet.model.Reservation;
import com.projet.repository.ReservationRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationRepository reservationRepository = new ReservationRepository();

    /**
     * Liste toutes les réservations.
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
     * Récupère les réservations 'EN_ATTENTE' pour une date précise (Utile pour le Front avant simulation).
     */
    @GetMapping("/api/reservations/unassigned/{date}")
    public ResponseEntity<List<Reservation>> getUnassignedByDate(@PathVariable("date") String date) {
        try {
            List<Reservation> reservations = reservationRepository.findUnassignedByDate(date);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Crée une nouvelle réservation.
     * Note : On extrait l'id_hotel car c'est la clé étrangère physique en DB.
     */
    @PostMapping("/api/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        try {
            // 1. Sécurité : Statut par défaut
            if (reservation.getStatus() == null || reservation.getStatus().isEmpty()) {
                reservation.setStatus("EN_ATTENTE");
            }
            
            // 2. Utilisation de l'ID Hotel envoyé dans le JSON
            // On appelle la méthode save qui prend l'objet reservation
            // Elle utilisera en interne reservation.getIdHotel()
            Reservation created = reservationRepository.save(reservation);
            
            // 3. Rechargement complet 
            // Important pour récupérer le nom de l'hôtel et le libellé du lieu via les JOIN
            created = reservationRepository.findById(created.getIdReservation());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Récupère une réservation spécifique.
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
     * Mise à jour du statut ou des informations.
     */
    @PutMapping("/api/reservations/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable("id") int id, @RequestBody Reservation reservation) {
        try {
            // Si on ne change que le statut
            reservationRepository.updateStatus(id, reservation.getStatus());
            
            Reservation updated = reservationRepository.findById(id);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Supprime une réservation.
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