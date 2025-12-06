<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Donneurs</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">

        <header class="header">
            <h1>
                <img src="<c:url value='/assets/people-line.png'/>" alt="" width="24" height="24">
                Gestion des Donneurs
            </h1>
            <p class="subtitle">Liste complète des donneurs enregistrés</p>
        </header>

        <nav class="breadcrumb">
            <a href="${pageContext.request.contextPath}/">Accueil</a> / 
            <span>Donneurs</span>
        </nav>

        <!-- Messages de succès -->
        <c:if test="${not empty param.success}">
            <div class="alert alert-success">
                <c:choose>
                    <c:when test="${param.success == 'create'}">Donneur créé avec succès</c:when>
                    <c:when test="${param.success == 'update'}">Donneur modifié avec succès</c:when>
                    <c:when test="${param.success == 'delete'}">Donneur supprimé avec succès</c:when>
                    <c:otherwise>Opération réussie</c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <!-- Mini statistiques -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-label">
                    <img src="<c:url value='/assets/people-line.png'/>" alt="" width="24" height="24">
                    Total Donneurs
                </div>
                <div class="stat-number">${totalDonneurs}</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">
                    <img src="<c:url value='/assets/user-add.png'/>" alt="" width="24" height="24">
                    Donneurs Actifs
                </div>
                <div class="stat-number">${donneursActifs}</div>
            </div>
        </div>

        <!-- Bouton nouveau donneur -->
        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/donneur?action=new" class="btn btn-success">
                <img src="<c:url value='/assets/user-add.png'/>" alt="" width="24" height="24">
                Nouveau Donneur
            </a>
        </div>

        <!-- Tableau des donneurs -->
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Nom Complet</th>
                        <th>Email</th>
                        <th>Groupe Sanguin</th>
                        <th>Dons</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="d" items="${donneurs}">
                        <tr>
                            <td><strong>#${d.id}</strong></td>
                            <td>${d.nomComplet}</td>
                            <td>${d.email}</td>
                            <td><strong>${d.groupeSanguin.designation}</strong></td>
                            <td>${d.nombreDons}</td>
                            <td>
                                <span class="badge badge-${d.actif ? 'actif' : 'inactif'}">
                                    ${d.actif ? 'Actif' : 'Inactif'}
                                </span>
                            </td>
                            <td class="action-btns">
                                <a href="${pageContext.request.contextPath}/donneur?action=view&id=${d.id}" 
                                   class="btn btn-sm btn-primary" title="Voir">
                                    <img src="<c:url value='/assets/eye.png'/>" alt="" width="18" height="18">
                                </a>
                                <a href="${pageContext.request.contextPath}/donneur?action=edit&id=${d.id}" 
                                   class="btn btn-sm btn-warning" title="Modifier">
                                    <img src="<c:url value='/assets/edit.png'/>" alt="" width="18" height="18">
                                </a>
                                <a href="${pageContext.request.contextPath}/donneur?action=delete&id=${d.id}" 
                                   class="btn btn-sm btn-danger btn-delete" title="Supprimer"
                                   onclick="return confirm('Supprimer définitivement ce donneur ?')">
                                    <img src="<c:url value='/assets/trash.png'/>" alt="" width="18" height="18">
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <c:if test="${empty donneurs}">
                <div class="alert alert-info" style="text-align:center; margin:40px 0;">
                    Aucun donneur enregistré pour le moment.
                </div>
            </c:if>
        </div>

        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                Retour à l'accueil
            </a>
        </div>

    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>