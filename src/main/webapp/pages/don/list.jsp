<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Dons</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <h1>💉 Gestion des Dons</h1>
            <p class="subtitle">Suivi des dons de sang et d'organes</p>
        </header>

        <nav class="breadcrumb">
            <a href="${pageContext.request.contextPath}/">Accueil</a> / 
            <span>Dons</span>
        </nav>

        <c:if test="${not empty param.success}">
            <div class="alert alert-success">
                <c:choose>
                    <c:when test="${param.success == 'create'}">✓ Don enregistré avec succès</c:when>
                    <c:when test="${param.success == 'update'}">✓ Don modifié avec succès</c:when>
                    <c:when test="${param.success == 'cancel'}">✓ Don annulé avec succès</c:when>
                    <c:when test="${param.success == 'attribue'}">✓ Don attribué avec succès</c:when>
                    <c:when test="${param.success == 'utilise'}">✓ Don marqué comme utilisé</c:when>
                </c:choose>
            </div>
        </c:if>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-label">Total Dons</div>
                <div class="stat-number">${totalDons}</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Dons ce Mois</div>
                <div class="stat-number">${donsMoisCourant}</div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Sang Disponible</div>
                <div class="stat-number">${quantiteSangDisponible} ml</div>
            </div>
        </div>

        <div class="action-bar">
            <a href="${pageContext.request.contextPath}/don?action=new" class="btn btn-success">
                ➕ Nouveau Don
            </a>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Type de Don</th>
                        <th>Date</th>
                        <th>Donneur ID</th>
                        <th>Receveur ID</th>
                        <th>Quantité</th>
                        <th>Lieu</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="don" items="${dons}">
                        <tr>
                            <td>${don.id}</td>
                            <td><strong>${don.typeDon.libelle}</strong></td>
                            <td>${don.dateDon}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/donneur?action=view&id=${don.donneurId}">
                                    #${don.donneurId}
                                </a>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${don.receveurId != null}">
                                        <a href="${pageContext.request.contextPath}/receveur?action=view&id=${don.receveurId}">
                                            #${don.receveurId}
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge">Non attribué</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${don.typeDon.estDonSang()}">
                                        ${don.quantite} ml
                                    </c:when>
                                    <c:otherwise>
                                        ${don.organe}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${don.lieuDon}</td>
                           <td>
							    <c:choose>
							        <c:when test="${don.statut.toString() == 'PROGRAMME'}">
							            <span class="badge badge-warning">${don.statut.libelle}</span>
							        </c:when>
							        <c:when test="${don.statut.toString() == 'COMPLETE' || don.statut.toString() == 'EN_STOCK'}">
							            <span class="badge badge-actif">${don.statut.libelle}</span>
							        </c:when>
							        <c:when test="${don.statut.toString() == 'ATTRIBUE' || don.statut.toString() == 'UTILISE'}">
							            <span class="badge badge-satisfait">${don.statut.libelle}</span>
							        </c:when>
							        <c:when test="${don.statut.toString() == 'ANNULE'}">
							            <span class="badge badge-inactif">${don.statut.libelle}</span>
							        </c:when>
							        <c:otherwise>
							            <span class="badge">${don.statut.libelle}</span>
							        </c:otherwise>
							    </c:choose>
							</td>
                            <td class="action-btns">
                                <a href="${pageContext.request.contextPath}/don?action=view&id=${don.id}" 
                                   class="btn btn-sm btn-primary">👁️ Voir</a>
                                <c:if test="${don.receveurId == null && don.statut != 'ANNULE'}">
                                    <a href="${pageContext.request.contextPath}/don?action=attribuer&id=${don.id}" 
                                       class="btn btn-sm btn-success">🔗 Attribuer</a>
                                </c:if>
                                <c:if test="${don.statut == 'PROGRAMME'}">
                                    <a href="${pageContext.request.contextPath}/don?action=edit&id=${don.id}" 
                                       class="btn btn-sm btn-warning">✏️ Modifier</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <c:if test="${empty dons}">
                <div class="alert alert-info">
                    Aucun don enregistré pour le moment.
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