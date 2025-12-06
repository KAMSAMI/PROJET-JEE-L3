<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>${receveur == null ? 'Nouveau' : 'Modifier'} Receveur</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <h1>${receveur == null ? '➕ Nouveau Receveur' : '✏️ Modifier Receveur'}</h1>
        </header>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">✗ ${errorMessage}</div>
        </c:if>

        <div class="form-container">
            <form method="post" action="${pageContext.request.contextPath}/receveur" data-validate="true">
                <input type="hidden" name="action" value="${receveur == null ? 'insert' : 'update'}">
                <c:if test="${receveur != null}">
                    <input type="hidden" name="id" value="${receveur.id}">
                </c:if>

                <h3>Informations Personnelles</h3>
                
                <div class="form-group">
                    <label for="nom">Nom *</label>
                    <input type="text" id="nom" name="nom" class="form-control" 
                           value="${receveur.nom}" required>
                </div>

                <div class="form-group">
                    <label for="prenom">Prénom *</label>
                    <input type="text" id="prenom" name="prenom" class="form-control" 
                           value="${receveur.prenom}" required>
                </div>

                <div class="form-group">
                    <label for="email">Email *</label>
                    <input type="email" id="email" name="email" class="form-control" 
                           value="${receveur.email}" required>
                </div>

                <div class="form-group">
                    <label for="telephone">Téléphone *</label>
                    <input type="tel" id="telephone" name="telephone" class="form-control" 
                           value="${receveur.telephone}" required>
                </div>

                <div class="form-group">
                    <label for="dateNaissance">Date de Naissance</label>
                    <input type="date" id="dateNaissance" name="dateNaissance" class="form-control" 
                           value="${receveur.dateNaissance}">
                </div>

                <div class="form-group">
                    <label for="groupeSanguin">Groupe Sanguin *</label>
                    <select id="groupeSanguin" name="groupeSanguin" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <c:forEach var="type" items="${typesSanguins}">
                            <option value="${type.designation}" 
                                    ${receveur.groupeSanguin == type ? 'selected' : ''}>
                                ${type.designation}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <h3>Besoins Médicaux</h3>

                <div class="form-group">
                    <label for="typeBesoin">Type de Besoin *</label>
                    <select id="typeBesoin" name="typeBesoin" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <c:forEach var="type" items="${typesBesoins}">
                            <option value="${type}" ${receveur.typeBesoin == type ? 'selected' : ''}>
                                ${type.libelle}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="organeNecessaire">Organe Nécessaire (si applicable)</label>
                    <input type="text" id="organeNecessaire" name="organeNecessaire" 
                           class="form-control" value="${receveur.organeNecessaire}"
                           placeholder="Ex: Rein, Foie, Cœur...">
                </div>

                <div class="form-group">
                    <label for="urgence">Niveau d'Urgence *</label>
                    <select id="urgence" name="urgence" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <c:forEach var="niveau" items="${niveauxUrgence}">
                            <option value="${niveau}" ${receveur.urgence == niveau ? 'selected' : ''}>
                                ${niveau.libelle}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="statut">Statut *</label>
                    <select id="statut" name="statut" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <c:forEach var="s" items="${statuts}">
                            <option value="${s}" ${receveur.statut == s ? 'selected' : ''}>
                                ${s.libelle}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="detailsMedicaux">Détails Médicaux</label>
                    <textarea id="detailsMedicaux" name="detailsMedicaux" 
                              class="form-control">${receveur.detailsMedicaux}</textarea>
                </div>

                <h3>Adresse</h3>

                <div class="form-group">
                    <label for="adresse">Adresse</label>
                    <input type="text" id="adresse" name="adresse" class="form-control" 
                           value="${receveur.adresse}">
                </div>

                <div class="form-group">
                    <label for="ville">Ville</label>
                    <input type="text" id="ville" name="ville" class="form-control" 
                           value="${receveur.ville}">
                </div>

                <div class="form-group">
                    <label for="codePostal">Code Postal</label>
                    <input type="text" id="codePostal" name="codePostal" class="form-control" 
                           value="${receveur.codePostal}">
                </div>

                <div class="form-group">
                    <label for="observations">Observations</label>
                    <textarea id="observations" name="observations" 
                              class="form-control">${receveur.observations}</textarea>
                </div>

                <div class="form-group">
                    <button type="submit" class="btn btn-success">
                        ${receveur == null ? '➕ Créer' : '💾 Enregistrer'}
                    </button>
                    <a href="${pageContext.request.contextPath}/receveur?action=list" 
                       class="btn btn-secondary">✖️ Annuler</a>
                </div>
            </form>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>