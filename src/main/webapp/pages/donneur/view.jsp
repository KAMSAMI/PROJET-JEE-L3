<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails du Donneur #${donneur.id}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">

        <header class="header">
            <h1>
                <img src="<c:url value='/assets/people-line.png'/>" alt="" width="24" height="24">
                Profil du Donneur
            </h1>
            <p class="subtitle">
                <strong>${donneur.nomComplet}</strong> — #${donneur.id}
            </p>
        </header>

        <nav class="breadcrumb">
            <a href="${pageContext.request.contextPath}/">Accueil</a> /
            <a href="${pageContext.request.contextPath}/donneur?action=list">Donneurs</a> /
            <span>#${donneur.id}</span>
        </nav>

        <div class="form-container">

            <!-- Informations personnelles -->
            <div class="info-section">
                <h3>
                    <img src="<c:url value='/assets/user-card.png'/>" alt="" width="24" height="24">
                    Informations personnelles
                </h3>
                <p><strong>Email :</strong> ${donneur.email}</p>
                <p><strong>Téléphone :</strong> ${donneur.telephone}</p>
                <p><strong>Date de naissance :</strong> ${donneur.dateNaissance}</p>
                <p><strong>Âge :</strong> <strong>${donneur.age} ans</strong></p>
                <p><strong>Groupe sanguin :</strong> 
                    <span style="font-size:1.6em; color:#e63946; font-weight:bold;">
                        ${donneur.groupeSanguin.designation}
                    </span>
                </p>
                <p><strong>Statut :</strong> 
                    <span class="badge badge-${donneur.actif ? 'actif' : 'inactif'}">
                        ${donneur.actif ? 'Actif' : 'Inactif'}
                    </span>
                </p>
            </div>

            <!-- Adresse -->
            <c:if test="${not empty donneur.adresse || not empty donneur.ville || not empty donneur.codePostal}">
                <div class="info-section">
                    <h3>
                        <img src="<c:url value='/assets/location-pin.png'/>" alt="" width="24" height="24">
                        Adresse
                    </h3>
                    <p><strong>Rue :</strong> ${donneur.adresse}</p>
                    <p><strong>Ville :</strong> ${donneur.ville}</p>
                    <p><strong>Code postal :</strong> ${donneur.codePostal}</p>
                </div>
            </c:if>

            <!-- Historique de dons -->
            <div class="info-section">
                <h3>
                    <img src="<c:url value='/assets/blood-donation.png'/>" alt="" width="24" height="24">
                    Historique de dons
                </h3>
                <p><strong>Nombre total de dons :</strong> <strong>${donneur.nombreDons}</strong></p>
                <p><strong>Date d'inscription :</strong> ${donneur.dateInscription}</p>
                <c:if test="${donneur.nombreDons > 0}">
                    <p><strong>Dernier don :</strong> ${donneur.dateDernierDon}</p>
                </c:if>
            </div>

            <!-- Observations -->
            <c:if test="${not empty donneur.observations}">
                <div class="info-section">
                    <h3>
                        <img src="<c:url value='/assets/document.png'/>" alt="" width="24" height="24">
                        Observations médicales
                    </h3>
                    <p style="white-space: pre-line; background:#f8f9fa; padding:15px; border-radius:8px;">
                        ${donneur.observations}
                    </p>
                </div>
            </c:if>

            <!-- Compatibilité sanguine -->
            <div class="info-section">
                <h3>
                    <img src="<c:url value='/assets/blood-donation.png'/>" alt="" width="24" height="24">
                    Compatibilité sanguine
                </h3>
                <p><strong>Peut donner à :</strong> 
                    <span style="color:#06d6a0; font-weight:600;">
                        ${donneur.groupeSanguin.compatibiliteDon}
                    </span>
                </p>
                <p><strong>Peut recevoir de :</strong> 
                    <span style="color:#e63946; font-weight:600;">
                        ${donneur.groupeSanguin.compatibiliteReception}
                    </span>
                </p>
            </div>

            <!-- Actions -->
            <div class="action-bar" style="margin-top:40px; display:flex; gap:15px; flex-wrap:wrap;">
                <a href="${pageContext.request.contextPath}/donneur?action=edit&id=${donneur.id}" 
                   class="btn btn-warning">
                    <img src="<c:url value='/assets/edit.png'/>" alt="" width="24" height="24">
                    Modifier le donneur
                </a>

                <a href="${pageContext.request.contextPath}/don?action=new&donneurId=${donneur.id}" 
                   class="btn btn-success">
                    <img src="<c:url value='/assets/blood-donation.png'/>" alt="" width="24" height="24">
                    Enregistrer un nouveau don
                </a>

                <a href="${pageContext.request.contextPath}/donneur?action=list" 
                   class="btn btn-secondary">
                    Retour à la liste
                </a>
            </div>

        </div>

    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>