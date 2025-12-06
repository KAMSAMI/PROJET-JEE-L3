<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Système de Suivi de Don de Sang et d'Organes</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
    <div class="container">
        <header class="header">
            <h1>🩸 Système de Suivi de Don de Sang et d'Organes</h1>
            <p class="subtitle">Gestion complète des dons, donneurs et receveurs</p>
        </header>

        <nav class="main-nav">
            <a href="<c:url value='/dashboard'/>" class="nav-btn dashboard-btn">
                <img src="<c:url value='/assets/dashboard-panel.png'/>" alt="" class="nav-icon" width="24" height="24"/> Tableau de Bord
            </a>
            <a href="<c:url value='/donneur?action=list'/>" class="nav-btn donneurs-btn">
                <img src="<c:url value='/assets/handle-with-care.png'/>" alt="" class="nav-icon" width="24" height="24"/> Gestion des Donneurs
            </a>
            <a href="<c:url value='/receveur?action=list'/>" class="nav-btn receveurs-btn">
                <img src="<c:url value='/assets/love.png'/>" alt="" class="nav-icon" width="24" height="24"/> Gestion des Receveurs
            </a>
            <a href="<c:url value='/don?action=list'/>" class="nav-btn dons-btn">
                <img src="<c:url value='/assets/blood-donation.png'/>" alt="" class="nav-icon" width="24" height="24"/> Gestion des Dons
            </a>
        </nav>

        <section class="info-section">
            <div class="info-card">
                <h2>🎯 Objectif du Système</h2>
                <p>
                    Ce système permet de gérer efficacement les dons de sang et d'organes,
                    en facilitant le suivi des donneurs, des receveurs et de leurs besoins
                    spécifiques. Notre plateforme aide à sauver des vies en optimisant
                    la gestion des ressources médicales vitales.
                </p>
            </div>

            <div class="features-grid">
                <div class="feature-card">
                    <h3><img src="<c:url value='/assets/handle-with-care.png'/>" alt="" class="feature-icon" width="24" height="24"/> Gestion des Donneurs</h3>
                    <ul>
                        <li>Inscription et profils des donneurs</li>
                        <li>Historique complet des dons</li>
                        <li>Recherche par nom ou groupe sanguin</li>
                        <li>Suivi de l'activité des donneurs</li>
                    </ul>
                </div>

                <div class="feature-card">
                    <h3><img src="<c:url value='/assets/love.png'/>" alt="" class="feature-icon" width="24" height="24"/> Gestion des Receveurs</h3>
                    <ul>
                        <li>Enregistrement des besoins médicaux</li>
                        <li>Niveaux d'urgence prioritaires</li>
                        <li>Suivi de la satisfaction des besoins</li>
                        <li>Association automatique avec les dons</li>
                    </ul>
                </div>

                <div class="feature-card">
                    <h3><img src="<c:url value='/assets/syringe.png'/>" alt="" class="feature-icon" width="24" height="24"/> Suivi des Dons</h3>
                    <ul>
                        <li>Enregistrement des dons de sang et d'organes</li>
                        <li>Vérification de compatibilité</li>
                        <li>Gestion des stocks disponibles</li>
                        <li>Historique et statistiques détaillées</li>
                    </ul>
                </div>

                <div class="feature-card">
                    <h3><img src="<c:url value='/assets/dashboard-panel.png'/>" alt="" class="feature-icon" width="24" height="24"/> Tableau de Bord</h3>
                    <ul>
                        <li>Statistiques en temps réel</li>
                        <li>Besoins urgents et priorités</li>
                        <li>Graphiques et visualisations</li>
                        <li>Rapports d'activité</li>
                    </ul>
                </div>
            </div>
        </section>

        <section class="blood-types-section">
            <h2>🩸 Groupes Sanguins et Compatibilité</h2>
            <div class="blood-types-grid">
                <div class="blood-card">
                    <h3>O-</h3>
                    <p class="universal">Donneur Universel</p>
                    <p>Peut donner à tous les groupes</p>
                </div>
                <div class="blood-card">
                    <h3>AB+</h3>
                    <p class="universal">Receveur Universel</p>
                    <p>Peut recevoir de tous les groupes</p>
                </div>
            </div>
        </section>

        <footer class="footer">
            <p>&copy; 2025 Système de Suivi de Don de Sang et d'Organes - JEE Project</p>
            <p>Développé avec Java, Servlets, JSP et Eclipse</p>
        </footer>
    </div>

    <script src="<c:url value='/js/main.js'/>"></script>
</body>
</html>