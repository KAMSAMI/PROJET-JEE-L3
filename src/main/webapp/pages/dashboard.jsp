<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de Bord</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <h1>📊 Tableau de Bord</h1>
            <p class="subtitle">Vue d'ensemble du système de don</p>
        </header>

        <nav class="breadcrumb">
            <a href="${pageContext.request.contextPath}/">Accueil</a> / 
            <span>Tableau de Bord</span>
        </nav>

        <h2>📈 Statistiques Générales</h2>
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-label">👥 Total Donneurs</div>
                <div class="stat-number">${totalDonneurs}</div>
                <small>${donneursActifs} actifs</small>
            </div>
            
            <div class="stat-card">
                <div class="stat-label">🏥 Total Receveurs</div>
                <div class="stat-number">${totalReceveurs}</div>
                <small>${receveursEnAttente} en attente</small>
            </div>
            
            <div class="stat-card">
                <div class="stat-label">💉 Total Dons</div>
                <div class="stat-number">${totalDons}</div>
                <small>${donsMoisCourant} ce mois</small>
            </div>
            
            <div class="stat-card">
                <div class="stat-label">🩸 Sang Disponible</div>
                <div class="stat-number">${quantiteSangDisponible} ml</div>
                <small>${donsDisponibles} dons en stock</small>
            </div>
        </div>

        <div class="alert alert-warning" style="margin: 20px 0;">
            <strong>⚠️ Attention:</strong> ${receveursCritiques} receveur(s) en situation critique !
            <a href="${pageContext.request.contextPath}/receveur?action=list" class="btn btn-sm btn-danger">
                Voir les urgences
            </a>
        </div>

        <div class="features-grid">
            <div class="feature-card">
                <h3>🏆 Top Donneurs</h3>
                <c:forEach var="donneur" items="${topDonneurs}">
                    <p>
                        <strong>${donneur.nomComplet}</strong> - 
                        ${donneur.nombreDons} don(s) - 
                        ${donneur.groupeSanguin.designation}
                    </p>
                </c:forEach>
            </div>

            <div class="feature-card">
                <h3>💉 Dons Récents</h3>
                <c:forEach var="don" items="${donsRecents}">
                    <p>
                        <strong>${don.typeDon.libelle}</strong> - 
                        ${don.dateDon} - 
                        <span class="badge badge-${don.statut}">${don.statut.libelle}</span>
                    </p>
                </c:forEach>
            </div>

            <div class="feature-card">
                <h3>🚨 Receveurs Urgents</h3>
                <c:choose>
                    <c:when test="${not empty receveursUrgents}">
                        <c:forEach var="receveur" items="${receveursUrgents}">
                            <p>
                                <strong>${receveur.nomComplet}</strong> - 
                                ${receveur.groupeSanguin.designation} - 
                                <span class="badge badge-urgent">${receveur.urgence.libelle}</span>
                            </p>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p class="alert alert-success">Aucun cas urgent pour le moment ✓</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="feature-card">
                <h3>📊 Actions Rapides</h3>
                <a href="${pageContext.request.contextPath}/donneur?action=new" class="btn btn-success" style="width: 100%; margin-bottom: 10px;">
                    ➕ Nouveau Donneur
                </a>
                <a href="${pageContext.request.contextPath}/receveur?action=new" class="btn btn-success" style="width: 100%; margin-bottom: 10px;">
                    ➕ Nouveau Receveur
                </a>
                <a href="${pageContext.request.contextPath}/don?action=new" class="btn btn-success" style="width: 100%;">
                    💉 Enregistrer un Don
                </a>
            </div>
        </div>

        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">← Retour à l'accueil</a>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>