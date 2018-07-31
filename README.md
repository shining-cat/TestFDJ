# TestFDJ
Exercice Paris sportif Android
Concevoir une application sur le football, à partir d’une API en accès libre.

Documentation de l’API : https://www.thesportsdb.com/api.php

## Note personnelle : 
  les noms de "championnat" utilisés par l'API semblent un peu différent de ce que l'utilisateur pourrait rechercher.
  L'API ne fait pas à proprement parler de recherche, au contraire, les requêtes sont traitées en correspondance exacte.
  par exemple dans les maquettes l'utilisateur recherche "ligue 1" ou "liga", ce qui ne donnerait aucun résultat, il faut
  rechercher "french ligue 1" ou "spanish la liga"...
  Le problème est que la fonctionnalité de suggestion de termes de recherche nécessite un data provider et ne peut pas être
  branchée directement sur le résultat d'une requête en JSON, il faudrait donc insérer en base de données la liste des
  "championnats" (qui est fournie par l'API), ce uniquement pour permettre une fonctionnalité non demandée explicitement
  dans les spécifications, ce qui me semble sortir un peu du cadre de l'exercice.

## Consignes :

### Sur la Home page :
    1- L’utilisateur cherche un championnat
    2- Appel vers API
    3- Traitement de la réponse
    4- Affichage de la liste
### Sur la liste des joueurs
    5- L’utilisateur sélectionne une équipe
    6- Appel vers API
    7- Traitement de la réponse
    8- Affichage de la liste
### Données à afficher pour les joueurs (toutes ces informations sont disponibles via l’API) :
    - Nom / Prénom
    - Position
    - Date de naissance
    - Montant du transfert
    - Image centrée sur visage (à défaut ; thumbnail du joueur)
