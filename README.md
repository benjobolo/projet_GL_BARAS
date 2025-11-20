# 🌍 ExchangeApp - Plateforme d'Échange Local

[![CI/CD Pipeline](https://github.com/benjobolo/projet_GL_BARAS/actions/workflows/ci.yml/badge.svg)](https://github.com/benjobolo/projet_GL_BARAS/actions/workflows/ci.yml)

> **Projet de Génie Logiciel - Master Informatique**  
> Une application web pour partager des objets et des compétences au sein d'une communauté locale.

---

## 📋 Table des matières

- [Description](#description)
- [Fonctionnalités](#fonctionnalités)
- [Technologies utilisées](#technologies-utilisées)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Tests](#tests)
- [Architecture](#architecture)

---

## 📖 Description

ExchangeApp est une plateforme d'échange hyperlocal qui permet aux utilisateurs de :
- Publier des annonces pour **prêter ou donner des objets**
- Proposer leurs **compétences** (bricolage, jardinage, cours, etc.)
- Envoyer des **demandes d'échange**
- Communiquer via une **messagerie intégrée**
- Laisser des **avis** après un échange

L'objectif est de créer des **communautés plus solidaires** en réduisant la consommation et en favorisant les liens sociaux.

---

## ✨ Fonctionnalités

### 👥 Gestion des utilisateurs
- Inscription avec email, nom, localisation
- Profil personnalisable (bio, photo, téléphone)
- Historique des échanges et des avis reçus

### 📢 Annonces
- Création d'annonces de type **OBJET** ou **COMPETENCE**
- Catégorisation (Bricolage, Jardinage, Électronique, etc.)
- Recherche par mot-clé
- Filtre par disponibilité

### 🤝 Demandes d'échange
- Envoyer une demande sur une annonce
- Proposer un objet/compétence en échange
- Statuts : PENDING, ACCEPTED, REFUSED
- Accepter/refuser les demandes

### 💬 Messagerie
- Chat entre utilisateurs
- Messages liés aux demandes d'échange
- Notification de messages non lus

### ⭐ Système d'avis
- Note de 1 à 5 étoiles
- Commentaire textuel
- Calcul de la note moyenne d'un utilisateur
- Un seul avis par échange

---

## 🛠️ Technologies utilisées

| Technologie | Version | Usage |
|-------------|---------|-------|
| **Java** | 17 | Langage backend |
| **Spring Boot** | 3.x | Framework web |
| **Spring Data JPA** | - | Accès aux données |
| **H2 Database** | - | Base de données en mémoire |
| **Thymeleaf** | - | Moteur de templates HTML |
| **Maven** | - | Gestionnaire de dépendances |
| **JUnit 5** | - | Tests unitaires |
| **Mockito** | - | Mock pour les tests |
| **GitHub Actions** | - | CI/CD |

---

## 🚀 Installation

### Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- Git

### Étapes d'installation

1. **Cloner le projet**
   ```bash
   git clone https://github.com/benjobolo/projet_GL_BARAS.git
   cd projet_GL_BARAS
   ```

2. **Compiler le projet**
   ```bash
   ./mvnw clean install
   ```

3. **Lancer l'application**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Accéder à l'application**
    - Application : http://localhost:8080
    - Console H2 : http://localhost:8080/h2-console
        - JDBC URL : `jdbc:h2:mem:exchangedb`
        - Username : `sa`
        - Password : *(laisser vide)*

---

## 💻 Utilisation

### Page d'accueil
Accédez à http://localhost:8080 pour voir le tableau de bord principal.

### Navigation
- **Utilisateurs** : `/users` - Gérer les utilisateurs
- **Annonces** : `/announcements` - Consulter/créer des annonces
- **Demandes** : `/exchange-requests` - Voir les demandes d'échange
- **Messages** : `/messages` - Messagerie
- **Avis** : `/reviews` - Système de notation

---

## 🧪 Tests

Le projet contient **49 tests** (unitaires + intégration) avec une couverture complète.

### Lancer tous les tests