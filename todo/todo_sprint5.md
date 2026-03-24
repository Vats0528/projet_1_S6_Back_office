TL : ETU002647 / Nalitiana
FO : 
BO : 




[Objectif]
-appliquer les temps d attentes TA
1 - TA pour le regroupement des reservation avant chaque depart
2 - TA_s = TA supplementaire ,c est le temps que met les vehicule pour arriver a l aeroport et
            se preparer a partir 
-fix des fonction pour adapter notre reservation vehicule en tant que trajet (déja mis en place pendant le sprint3 )

[A_faire]
[TL]
    [x]- creation de la branche sur github
    [x]- script et mis a jour des relations sql
    [x]- donnee de simulation sql
    [ ]- GIT flow

[FO]
    [x]-mise a jour des DTO et la reception de donnee
        [/]-ajouter la column TA_s dans la page param
        [x]-(optionnel)afficher le TA dans l affichage des assignation vehicule
[BO]
    [x]-fix la logique de TA (satry efa nisy TA tao fa tsy marina), dans service et les repository
    [x]-mise a jour des MODEL/CONTROLLER et l envoie des donnee
        [ ]-ajouter la column TA_s
    [x]-simulation sprint 5
    [x]-simulation sprint 6 (vehicule redisponible avec comparaison des nb trajet (dans la meme date))
    


STATUS : [finished]
