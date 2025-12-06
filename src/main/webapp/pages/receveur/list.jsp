<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Receveurs</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <h1>🏥 Gestion des Receveurs</h1>
            <p class="subtitle">Liste des receveurs et leurs besoins</p>
        </header>

        <nav class="breadcrumb">
            <a href="${pageContext.request.contextPath}/">Accueil</a> / 
            <span>Receveurs</span>
        </nav>

        <c:if test="${not empty param.success}">
            <div class="alert alert-success">
                <c:choose>
                    <c:when test="${param.success == 'create'}">✓ Receveur créé avec succès</c:when>
                    <c:when test="${param.success == 'update'}">✓ Receveur modifié avec succès</c:when>
                    <c:when test="${param.success == 'delete'}">✓ Receveur supprimé avec succès</c:when>
                </c:choose>
            </div>
        </c:if>

        <c:if test="${receveursCritiques > 0}">
            <div class="alert alert-danger">
                <strong>⚠️ ALERTE:</strong> ${receveursCritiques} receveur(s) en situation critique !
            </div>
        </c:if>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-label">Total Receveurs</div>
                <div class="stat-number">${totalReceveurs}</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">En Attente</div>
                <div class="stat-number">${receveursEnAttente}</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Cas Critiques</div>
                <div class="stat-number" style="color: #dc3545;">${receveursCritiques}</div>
            </div>
        </div>

        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/receveur?action=new" class="btn btn-success">
                ➕ Nouveau Receveur
            </a>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom Complet</th>
                        <th>Email</th>
                        <th>Groupe Sanguin</th>
                        <th>Type de Besoin</th>
                        <th>Urgence</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="receveur" items="${receveurs}">
                        <tr>
                            <td>${receveur.id}</td>
                            <td>${receveur.nomComplet}</td>
                            <td>${receveur.email}</td>
                            <td><strong>${receveur.groupeSanguin.designation}</strong></td>
                            <td>${receveur.typeBesoin.libelle}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${receveur.urgence == 'CRITIQUE'}">
                                        <span class="badge badge-urgent">${receveur.urgence.libelle}</span>
                                    </c:when>
                                    <c:when test="${receveur.urgence == 'ELEVE'}">
                                        <span class="badge badge-warning">${receveur.urgence.libelle}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge">${receveur.urgence.libelle}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${receveur.statut == 'EN_ATTENTE'}">
                                        <span class="badge badge-attente">${receveur.statut.libelle}</span>
                                    </c:when>
                                    <c:when test="${receveur.statut == 'BESOIN_SATISFAIT'}">
                                        <span class="badge badge-satisfait">${receveur.statut.libelle}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge">${receveur.statut.libelle}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="action-btns">
                                <a href="${pageContext.request.contextPath}/receveur?action=view&id=${receveur.id}" 
                                   class="btn btn-sm btn-primary">👁️ Voir</a>
                                <a href="${pageContext.request.contextPath}/receveur?action=edit&id=${receveur.id}" 
                                   class="btn btn-sm btn-warning">✏️ Modifier</a>
                                <a href="${pageContext.request.contextPath}/receveur?action=delete&id=${receveur.id}" 
                                   class="btn btn-sm btn-danger btn-delete">🗑️ Supprimer</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <c:if test="${empty receveurs}">
                <div class="alert alert-info">
                    Aucun receveur enregistré pour le moment.
                </div>
            </c:if>
        </div>

        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">← Retour à l'accueil</a>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>