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

import com.projet.model.Client;
import com.projet.repository.ClientRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ClientController {

    private ClientRepository clientRepository = new ClientRepository();

    /**
     * GET /api/clients - Liste tous les clients
     */
    @GetMapping("/api/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        try {
            List<Client> clients = clientRepository.findAll();
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * GET /api/clients/{id} - Récupère un client par ID
     */
    @GetMapping("/api/clients/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable("id") int id) {
        try {
            Client client = clientRepository.findById(id);
            if (client != null) {
                return ResponseEntity.ok(client);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * POST /api/clients - Crée un nouveau client
     */
    @PostMapping("/api/clients")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        try {
            Client created = clientRepository.save(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * PUT /api/clients/{id} - Met à jour un client
     */
    @PutMapping("/api/clients/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable("id") int id, @RequestBody Client client) {
        try {
            client.setIdClient(id);
            Client updated = clientRepository.update(client);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * DELETE /api/clients/{id} - Supprime un client
     */
    @DeleteMapping("/api/clients/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") int id) {
        try {
            clientRepository.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
