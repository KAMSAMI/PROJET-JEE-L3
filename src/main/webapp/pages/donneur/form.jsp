<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${donneur == null ? 'Nouveau' : 'Modifier'} Donneur</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">

        <header class="header">
            <h1>
                <c:choose>
                    <c:when test="${donneur == null}">
                        <img src="<c:url value='/assets/user-add.png'/>" alt="" width="24" height="24">
                        Nouveau Donneur
                    </c:when>
                    <c:otherwise>
                        <img src="<c:url value='/assets/edit.png'/>" alt="" width="24" height="24">
                        Modifier le Donneur
                    </c:otherwise>
                </c:choose>
            </h1>
            <p class="subtitle">
                <c:if test="${donneur != null}">
                    #${donneur.id} — ${donneur.nomComplet}
                </c:if>
            </p>
        </header>

        <nav class="breadcrumb">
            <a href="${pageContext.request.contextPath}/">Accueil</a> /
            <a href="${pageContext.request.contextPath}/donneur?action=list">Donneurs</a> /
            <span>${donneur == null ? 'Nouveau' : 'Modifier'}</span>
        </nav>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                ${errorMessage}
            </div>
        </c:if>

        <div class="form-container">
            <!-- Ajout de data-validate="true" au cas où ton JS l'utilise -->
            <form method="post" action="${pageContext.request.contextPath}/donneur" data-validate="true">
                <input type="hidden" name="action" value="${donneur == null ? 'insert' : 'update'}">
                <c:if test="${donneur != null}">
                    <input type="hidden" name="id" value="${donneur.id}">
                </c:if>

                <h3>Informations personnelles</h3>

                <div class="form-group">
                    <label for="nom">Nom <span style="color:#e63946;">*</span></label>
                    <input type="text" id="nom" name="nom" class="form-control" value="${donneur.nom}" required>
                </div>

                <div class="form-group">
                    <label for="prenom">Prénom <span style="color:#e63946;">*</span></label>
                    <input type="text" id="prenom" name="prenom" class="form-control" value="${donneur.prenom}" required>
                </div>

                <div class="form-group">
                    <label for="email">Email <span style="color:#e63946;">*</span></label>
                    <input type="email" id="email" name="email" class="form-control" value="${donneur.email}" required>
                </div>

                <div class="form-group">
                    <label for="telephone">Téléphone <span style="color:#e63946;">*</span></label>
                    <input type="tel" id="telephone" name="telephone" class="form-control" value="${donneur.telephone}" required>
                </div>

                <div class="form-group">
                    <label for="dateNaissance">Date de naissance <span style="color:#e63946;">*</span></label>
                    <input type="date" id="dateNaissance" name="dateNaissance" class="form-control" value="${donneur.dateNaissance}" required>
                </div>

                <div class="form-group">
                    <label for="groupeSanguin">Groupe sanguin <span style="color:#e63946;">*</span></label>
                    <select id="groupeSanguin" name="groupeSanguin" class="form-control" required>
                        <option value="">-- Choisir le groupe sanguin --</option>
                        <c:forEach var="type" items="${typesSanguins}">
                            <option value="${type.designation}" 
                                    ${donneur.groupeSanguin == type ? 'selected' : ''}>
                                ${type.designation}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <h3>Adresse (facultatif)</h3>

                <div class="form-group">
                    <label for="adresse">Adresse complète</label>
                    <input type="text" id="adresse" name="adresse" class="form-control" value="${donneur.adresse}" placeholder="Rue, numéro...">
                </div>

                <div class="form-group">
                    <label for="ville">Ville</label>
                    <input type="text" id="ville" name="ville" class="form-control" value="${donneur.ville}">
                </div>

                <div class="form-group">
                    <label for="codePostal">Code postal</label>
                    <input type="text" id="codePostal" name="codePostal" class="form-control" value="${donneur.codePostal}">
                </div>

                <div class="form-group">
                    <label for="observations">Observations médicales ou remarques</label>
                    <textarea id="observations" name="observations" class="form-control" rows="4" placeholder="Antécédents, allergies, etc...">${donneur.observations}</textarea>
                </div>

                <!-- Checkbox corrigée : valeur "on" si cochée (standard HTML) -->
                <c:if test="${donneur != null}">
                    <div class="form-group">
                        <label style="font-weight:600;">
                            <input type="checkbox" name="actif" value="on" ${donneur.actif ? 'checked' : ''}>
                            Donneur actif (peut donner)
                        </label>
                    </div>
                </c:if>

                <div class="form-group" style="margin-top:40px; display:flex; gap:15px; flex-wrap:wrap;">
                    <button type="submit" class="btn btn-success">
                        <c:choose>
                            <c:when test="${donneur == null}">
                                <img src="<c:url value='/assets/user-add.png'/>" alt="" width="24" height="24">
                                Créer le donneur
                            </c:when>
                            <c:otherwise>
                                <img src="<c:url value='/assets/edit.png'/>" alt="" width="24" height="24">
                                Enregistrer les modifications
                            </c:otherwise>
                        </c:choose>
                    </button>

                    <a href="${pageContext.request.contextPath}/donneur?action=list" class="btn btn-secondary">
                        Annuler
                    </a>
                </div>
            </form>
        </div>

        <div class="action-bar" style="margin-top:40px;">
            <a href="${pageContext.request.contextPath}/donneur?action=list" class="btn btn-secondary">
                Retour à la liste des donneurs
            </a>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>