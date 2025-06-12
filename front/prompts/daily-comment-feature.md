# Fonctionnalité de Commentaires Journaliers

## Objectif
Permettre à un utilisateur d'enregistrer un seul commentaire par jour avec possibilité d'édition.

## Spécifications

### Modèle de données
- Un commentaire par utilisateur par jour
- Les commentaires sont liés à l'utilisateur qui les a créés
- Les commentaires peuvent être modifiés
- Chaque commentaire contient :
  - ID
  - ID de l'utilisateur
  - Contenu
  - Date
  - Date de dernière modification

### Fonctionnalités
1. Création d'un commentaire journalier
2. Modification d'un commentaire existant
3. Consultation du commentaire du jour
4. Historique des commentaires de l'utilisateur

### Architecture
- Utilisation de NgRx pour la gestion d'état
- Service dédié pour les appels API
- Actions et reducers pour la gestion des commentaires
- Effets pour la gestion des effets de bord

### Points d'API
- GET /api/daily-comments/date/:date
- POST /api/daily-comments
- PUT /api/daily-comments/:id
- GET /api/daily-comments/user/:userId

### Sécurité
- Vérification que l'utilisateur ne peut modifier que ses propres commentaires
- Un seul commentaire par jour par utilisateur


Je souhaite que tu enregistre le prompte dans le dossier /front/prompte

Il faut que tu ajoute les fonctionalité suivante:
L'objectif est de pouvoir permettre à un utilisateur d'enregistrer pour chaque jour un et un seul commentaire
Les commentaires peuvent être édités

Pour ce faire les commentaire seront envoyer au back par le store
Les commentaire sont liées a l’utilisateur qui les a envoyer
Je veux que tu creer directement les fichiers dans mon application actuelle