<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails du Receveur</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <h1>🏥 Profil du Receveur</h1>
        </header>

        <div class="form-container">
            <h2>${receveur.nomComplet}</h2>
            
            <div class="info-section">
                <h3>📋 Informations Personnelles</h3>
                <p><strong>ID:</strong> ${receveur.id}</p>
                <p><strong>Email:</strong> ${receveur.email}</p>
                <p><strong>Téléphone:</strong> ${receveur.telephone}</p>
                <p><strong>Date de Naissance:</strong> ${receveur.dateNaissance}</p>
                <p><strong>Âge:</strong> ${receveur.age} ans</p>
                <p><strong>Groupe Sanguin:</strong> 
                    <span style="font-size: 1.5em; color: #e63946;">
                        ${receveur.groupeSanguin.designation}
                    </span>
                </p>
                <p><strong>Statut:</strong> 
                    <span class="badge">${receveur.statut.libelle}</span>
                </p>
            </div>

            <div class="info-section">
                <h3>🏥 Besoins Médicaux</h3>
                <p><strong>Type de Besoin:</strong> ${receveur.typeBesoin.libelle}</p>
                <c:if test="${not empty receveur.organeNecessaire}">
                    <p><strong>Organe Nécessaire:</strong> ${receveur.organeNecessaire}</p>
                </c:if>
                <p><strong>Urgence:</strong> 
                    <span class="badge badge-${receveur.urgence}">${receveur.urgence.libelle}</span>
                </p>
                <p><strong>Détails Médicaux:</strong> ${receveur.detailsMedicaux}</p>
            </div>

            <div class="info-section">
                <h3>📍 Adresse</h3>
                <p><strong>Adresse:</strong> ${receveur.adresse}</p>
                <p><strong>Ville:</strong> ${receveur.ville}</p>
                <p><strong>Code Postal:</strong> ${receveur.codePostal}</p>
            </div>

            <div class="info-section">
                <h3>💉 Historique</h3>
                <p><strong>Nombre de Dons Reçus:</strong> ${receveur.nombreDonsRecus}</p>
                <p><strong>Date d'Inscription:</strong> ${receveur.dateInscription}</p>
                <c:if test="${receveur.statut == 'BESOIN_SATISFAIT'}">
                    <p><strong>Date de Réception:</strong> ${receveur.dateReceptionDon}</p>
                </c:if>
            </div>

            <c:if test="${not empty receveur.observations}">
                <div class="info-section">
                    <h3>📝 Observations</h3>
                    <p>${receveur.observations}</p>
                </div>
            </c:if>

            <div class="info-section">
                <h3>🩸 Compatibilité Sanguine</h3>
                <p><strong>Peut recevoir de:</strong> ${receveur.groupeSanguin.compatibiliteReception}</p>
                <p><strong>Peut donner à:</strong> ${receveur.groupeSanguin.compatibiliteDon}</p>
            </div>

            <div class="action-bar">
                <a href="${pageContext.request.contextPath}/receveur?action=edit&id=${receveur.id}" 
                   class="btn btn-warning">✏️ Modifier</a>
                <a href="${pageContext.request.contextPath}/receveur?action=list" 
                   class="btn btn-secondary">← Retour à la liste</a>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>