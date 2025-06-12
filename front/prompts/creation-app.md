Prompt generer par IA

# Création de l'application Angular RememberMe

## Contraintes initiales
- Application dans un dossier `front` à la racine
- Utilisation de Tailwind CSS
- Dernière version d'Angular (v19)
- Store NgRx pour les requêtes API
- Tests unitaires avec Jest
- Page d'accueil avec header et menu latéral

## Structure créée

### Architecture
- **Composants standalone** : Utilisation des composants standalone d'Angular 19
- **NgRx Store** : Gestion d'état centralisée pour les utilisateurs
- **Services** : Service UserService pour les appels API
- **Routing** : Navigation entre les pages

### Composants principaux
1. **AppComponent** : Composant racine avec layout principal
2. **HeaderComponent** : En-tête avec bouton menu et titre
3. **SidebarComponent** : Menu latéral avec navigation
4. **HomeComponent** : Page d'accueil avec présentation
5. **UsersComponent** : Page de gestion des utilisateurs

### Store NgRx
- **Actions** : Actions pour CRUD utilisateurs
- **Reducers** : Gestion des états
- **Effects** : Gestion des effets de bord (appels API)
- **Selectors** : Sélecteurs pour accéder aux données

### Styling
- **Tailwind CSS** : Framework CSS utilitaire
- **Design responsive** : Adaptation mobile/desktop
- **Thème cohérent** : Couleurs primaires et design moderne

### Tests
- **Jest** : Framework de test configuré
- **Tests unitaires** : Tests pour composants et services
- **Mocks** : Mocks pour HttpClient et autres dépendances

## Fonctionnalités implémentées
- Navigation responsive avec sidebar
- Page d'accueil avec cartes de présentation
- Page utilisateurs avec tableau et gestion d'état
- Appels API vers le backend Spring Boot
- Gestion des erreurs et états de chargement
- Tests unitaires de base

## Prochaines étapes possibles
- Formulaires de création/modification d'utilisateurs
- Pagination et filtres
- Authentification
- Notifications toast
- Tests d'intégration