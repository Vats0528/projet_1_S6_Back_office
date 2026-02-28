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

import com.projet.model.Reservation;
import com.projet.repository.ReservationRepository;
import com.projet.service.TokenService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ReservationController {

    private ReservationRepository reservationRepository = new ReservationRepository();
    private TokenService tokenService = new TokenService();

    /**
     *  Vérification centralisée du token
     */
    private ResponseEntity<?> checkToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Token absent");
            }

            boolean isValid = tokenService.validateToken(token);

            if (!isValid) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Token invalide ou expiré");
            }

            return null; // Token valide

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Token révoqué ou accès interdit");
        }
    }

    /**
     * GET /main/{token}/api/reservations
     */
    @GetMapping("/main/{token}/api/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations(
            @PathVariable("token") String token) {

        ResponseEntity<?> security = checkToken(token);
        if (security != null)
            return (ResponseEntity<List<Reservation>>) security;

        try {
            List<Reservation> reservations = reservationRepository.findAll();
            return ResponseEntity.ok(reservations);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * GET /main/{token}/api/reservations/date/{date}
     * Format date: YYYY-MM-DD
     */
    @GetMapping("/main/{token}/api/reservations/date/{date}")
    public ResponseEntity<List<Reservation>> getReservationsByDate(
            @PathVariable("token") String token,
            @PathVariable("date") String date) {

        ResponseEntity<?> security = checkToken(token);
        if (security != null)
            return (ResponseEntity<List<Reservation>>) security;

        try {
            List<Reservation> reservations = reservationRepository.findByDate(date);
            return ResponseEntity.ok(reservations);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * GET /main/{token}/api/reservations/{id}
     */
    @GetMapping("/main/{token}/api/reservations/{id}")
    public ResponseEntity<Reservation> getReservationById(
            @PathVariable("token") String token,
            @PathVariable("id") int id) {

        ResponseEntity<?> security = checkToken(token);
        if (security != null)
            return (ResponseEntity<Reservation>) security;

        try {
            Reservation reservation = reservationRepository.findById(id);

            if (reservation != null) {
                return ResponseEntity.ok(reservation);
            }

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * POST /main/{token}/api/reservations
     */
    @PostMapping("/main/{token}/api/reservations")
    public ResponseEntity<Reservation> createReservation(
            @PathVariable("token") String token,
            @RequestBody Reservation reservation) {

        ResponseEntity<?> security = checkToken(token);
        if (security != null)
            return (ResponseEntity<Reservation>) security;

        try {
            Reservation created = reservationRepository.save(reservation);
            created = reservationRepository.findById(created.getIdReservation());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(created);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * PUT /main/{token}/api/reservations/{id}
     */
    @PutMapping("/main/{token}/api/reservations/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable("token") String token,
            @PathVariable("id") int id,
            @RequestBody Reservation reservation) {

        ResponseEntity<?> security = checkToken(token);
        if (security != null)
            return (ResponseEntity<Reservation>) security;

        try {
            reservation.setIdReservation(id);
            Reservation updated = reservationRepository.update(reservation);
            updated = reservationRepository.findById(id);

            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * DELETE /main/{token}/api/reservations/{id}
     */
    @DeleteMapping("/main/{token}/api/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable("token") String token,
            @PathVariable("id") int id) {

        ResponseEntity<?> security = checkToken(token);
        if (security != null)
            return (ResponseEntity<Void>) security;

        try {
            reservationRepository.delete(id);

            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(null);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}