SELECT * FROM vehicule 
WHERE id_vehicule NOT IN (
    SELECT id_vehicule 
    FROM reservation_vehicule 
    WHERE (date_heure_depart < ? AND date_heure_retour > ?)
);

-- vehicule disponible
SELECT * FROM vehicule 
WHERE id_vehicule NOT IN (
    SELECT id_vehicule 
    FROM reservation_vehicule 
    -- On cherche les réservations qui chevauchent mon créneau
    WHERE date_heure_depart < '2024-06-01 08:00:01' -- La fin de mon besoin
      AND date_heure_retour > '2024-06-01 08:00:00' -- Le début de mon besoin
);


SELECT 
    l.id_lieu, 
    l.code, 
    l.libelle, 
    dt.succession
FROM 
    details_trajet dt
JOIN 
    distance d ON dt.id_distance = d.id_distance
JOIN 
    lieu l ON (
        (dt.succession = 1 AND l.id_lieu = d.id_lieu) -- Départ du 1er trajet
        OR 
        l.id_lieu = d.id_lieu_1 -- Destinations de tous les segments
    )
WHERE 
    dt.id_reservation_vehicule = 1
ORDER BY 
    dt.succession ASC, 
    (l.id_lieu = d.id_lieu_1) ASC;



--duree trajet d un vehicule , en fonctin de distance et param vitess moyenne
    SELECT 
        (SUM(d.distance) / (SELECT vitess_moyenne FROM param_vehicule LIMIT 1)) * 60 as duree_calculee
    FROM details_trajet dt
    JOIN distance d ON dt.id_distance = d.id_distance
    WHERE dt.id_reservation_vehicule = ?;

-- duree trajet du vehicule en fonction de temps
    SELECT SUM(d.temps) as duree_totale
FROM details_trajet dt
JOIN distance d ON dt.id_distance = d.id_distance
WHERE dt.id_reservation_vehicule = ?;