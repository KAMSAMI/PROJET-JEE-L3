<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${don == null ? 'Nouveau' : 'Modifier'} Don</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <h1>${don == null ? '➕ Nouveau Don' : '✏️ Modifier Don'}</h1>
        </header>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                ✗ ${errorMessage}
            </div>
        </c:if>

        <div class="form-container">
            <form method="post" action="${pageContext.request.contextPath}/don" data-validate="true">
                <input type="hidden" name="action" value="${don == null ? 'insert' : 'update'}">
                <c:if test="${don != null}">
                    <input type="hidden" name="id" value="${don.id}">
                </c:if>

                <h3>Informations du Don</h3>
                
                <div class="form-group">
                    <label for="typeDon">Type de Don *</label>
                    <select id="typeDon" name="typeDon" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <c:forEach var="type" items="${typesDon}">
                            <option value="${type}" ${don.typeDon == type ? 'selected' : ''}>
                                ${type.libelle}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="dateDon">Date du Don *</label>
                    <input type="date" id="dateDon" name="dateDon" class="form-control" 
                           value="${don.dateDon}" required>
                </div>

                <div class="form-group">
                    <label for="donneurId">Donneur *</label>
                    <select id="donneurId" name="donneurId" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <c:forEach var="donneur" items="${donneurs}">
                            <option value="${donneur.id}" ${don.donneurId == donneur.id ? 'selected' : ''}>
                                ${donneur.nomComplet} (${donneur.groupeSanguin.designation})
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="statut">Statut *</label>
                    <select id="statut" name="statut" class="form-control" required>
                        <option value="">-- Sélectionner --</option>
                        <c:forEach var="s" items="${statutsDon}">
                            <option value="${s}" ${don.statut == s ? 'selected' : ''}>
                                ${s.libelle}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="quantite">Quantité (ml, pour sang)</label>
                    <input type="number" id="quantite" name="quantite" class="form-control" 
                           value="${don.quantite}" step="0.1">
                </div>

                <div class="form-group">
                    <label for="organe">Organe (pour don d'organe)</label>
                    <input type="text" id="organe" name="organe" class="form-control" 
                           value="${don.organe}">
                </div>

                <div class="form-group">
                    <label for="lieuDon">Lieu du Don</label>
                    <input type="text" id="lieuDon" name="lieuDon" class="form-control" 
                           value="${don.lieuDon}">
                </div>

                <div class="form-group">
                    <label for="observations">Observations</label>
                    <textarea id="observations" name="observations" class="form-control">${don.observations}</textarea>
                </div>

                <div class="form-group">
                    <button type="submit" class="btn btn-success">
                        ${don == null ? '➕ Créer' : '💾 Enregistrer'}
                    </button>
                    <a href="${pageContext.request.contextPath}/don?action=list" 
                       class="btn btn-secondary">✖️ Annuler</a>
                </div>
            </form>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>