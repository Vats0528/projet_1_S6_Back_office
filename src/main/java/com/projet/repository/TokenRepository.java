package com.projet.repository;

import com.projet.config.DatabaseConnection;
import com.projet.model.Token;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class TokenRepository {


    public boolean existsByValeur(String valeurToken) throws SQLException {
        String query = "SELECT COUNT(*) FROM token WHERE valeur_token = ?";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, valeurToken);

        ResultSet rs = stmt.executeQuery();
        rs.next();
        boolean exists = rs.getInt(1) > 0;

        rs.close();
        stmt.close();

        return exists;
    }


    public Token save(Token token) throws SQLException {
        String query = """
                INSERT INTO token (valeur_token, niveau, date_creation, date_expiration, est_actif)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id_token
                """;

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setString(1, token.getValeurToken());
        stmt.setString(2, token.getNiveau());
        stmt.setTimestamp(3, Timestamp.valueOf(token.getDateCreation()));
        stmt.setTimestamp(4, Timestamp.valueOf(token.getDateExpiration()));
        stmt.setBoolean(5, token.isEstActif());

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            token.setIdToken(rs.getInt("id_token"));
        }

        rs.close();
        stmt.close();

        return token;
    }


    public Optional<Token> findValidToken(String valeurToken) throws SQLException {
        String query = """
                SELECT * FROM token
                WHERE valeur_token = ?
                AND est_actif = TRUE
                AND date_expiration > NOW()
                """;

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, valeurToken);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Token token = new Token();
            token.setIdToken(rs.getInt("id_token"));
            token.setValeurToken(rs.getString("valeur_token"));
            token.setNiveau(rs.getString("niveau"));
            token.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
            token.setDateExpiration(rs.getTimestamp("date_expiration").toLocalDateTime());
            token.setEstActif(rs.getBoolean("est_actif"));

            rs.close();
            stmt.close();

            return Optional.of(token);
        }

        rs.close();
        stmt.close();
        return Optional.empty();
    }


    public void deactivate(String valeurToken) throws SQLException {
        String query = "UPDATE token SET est_actif = FALSE WHERE valeur_token = ?";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, valeurToken);
        stmt.executeUpdate();
        stmt.close();
    }
}