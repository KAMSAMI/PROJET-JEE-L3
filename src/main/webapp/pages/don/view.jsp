<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails du Don</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <h1>💉 Détails du Don</h1>
        </header>

        <c:if test="${not empty param.success}">
            <div class="alert alert-success">
                <c:choose>
                    <c:when test="${param.success == 'attribue'}">✓ Don attribué avec succès</c:when>
                    <c:when test="${param.success == 'utilise'}">✓ Don marqué comme utilisé</c:when>
                </c:choose>
            </div>
        </c:if>

        <div class="form-container">
            <h2>${don.typeDon.libelle} - #${don.numeroReference}</h2>
            
            <div class="info-section">
                <h3>📋 Informations Générales</h3>
                <p><strong>ID:</strong> ${don.id}</p>
                <p><strong>Date du Don:</strong> ${don.dateDon}</p>
                <p><strong>Heure Prélevée:</strong> ${don.heurePrelevee}</p>
                <p><strong>Lieu:</strong> ${don.lieuDon}</p>
                <p><strong>Statut:</strong> 
                    <span class="badge">${don.statut.libelle}</span>
                </p>
                <p><strong>Valide:</strong> ${don.estValide() ? 'Oui' : 'Non'}</p>
                <p><strong>Compatible:</strong> ${don.compatible ? 'Oui' : 'Non'}</p>
            </div>

            <div class="info-section">
                <h3>🔬 Détails Spécifiques</h3>
                <c:choose>
                    <c:when test="${don.typeDon.estDonSang()}">
                        <p><strong>Quantité:</strong> ${don.quantite} ml</p>
                    </c:when>
                    <c:otherwise>
                        <p><strong>Organe:</strong> ${don.organe}</p>
                    </c:otherwise>
                </c:choose>
                <p><strong>Prochain Don Autorisé:</strong> ${don.dateProchainDonAutorise}</p>
            </div>

            <div class="info-section">
                <h3>👤 Personnes Associées</h3>
                <p><strong>Donneur:</strong> 
                    <a href="${pageContext.request.contextPath}/donneur?action=view&id=${don.donneurId}">
                        ${donneur.nomComplet} (${donneur.groupeSanguin.designation})
                    </a>
                </p>
                <p><strong>Receveur:</strong> 
                    <c:choose>
                        <c:when test="${receveur != null}">
                            <a href="${pageContext.request.contextPath}/receveur?action=view&id=${don.receveurId}">
                                ${receveur.nomComplet} (${receveur.groupeSanguin.designation})
                            </a>
                        </c:when>
                        <c:otherwise>
                            Non attribué
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>

            <c:if test="${not empty don.observations}">
                <div class="info-section">
                    <h3>📝 Observations</h3>
                    <p>${don.observations}</p>
                </div>
            </c:if>

            <div class="action-bar">
                <c:if test="${don.statut == 'PROGRAMME'}">
                    <a href="${pageContext.request.contextPath}/don?action=edit&id=${don.id}" 
                       class="btn btn-warning">✏️ Modifier</a>
                </c:if>
                <c:if test="${don.receveurId == null && don.statut != 'ANNULE'}">
                    <a href="${pageContext.request.contextPath}/don?action=attribuer&id=${don.id}" 
                       class="btn btn-success">🔗 Attribuer</a>
                </c:if>
                <c:if test="${don.statut == 'ATTRIBUE'}">
                    <form method="post" action="${pageContext.request.contextPath}/don" style="display: inline;">
                        <input type="hidden" name="action" value="marquerUtilise">
                        <input type="hidden" name="id" value="${don.id}">
                        <button type="submit" class="btn btn-primary">✅ Marquer Utilisé</button>
                    </form>
                </c:if>
                <a href="${pageContext.request.contextPath}/don?action=list" 
                   class="btn btn-secondary">← Retour à la liste</a>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>