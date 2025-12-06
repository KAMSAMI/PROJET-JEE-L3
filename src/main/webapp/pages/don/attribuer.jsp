<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attribuer le Don</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <h1>🔗 Attribuer le Don #${don.id}</h1>
            <p>Type: ${don.typeDon.libelle} - Date: ${don.dateDon}</p>
        </header>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">✗ ${errorMessage}</div>
        </c:if>

        <div class="form-container">
            <form method="post" action="${pageContext.request.contextPath}/don">
                <input type="hidden" name="action" value="attribuer">
                <input type="hidden" name="donId" value="${don.id}">

                <h3>Sélectionner un Receveur</h3>

                <div class="form-group">
                    <label for="receveurId">Receveur *</label>
                    <select id="receveurId" name="receveurId" class="form-control" required>
                        <option value="">-- Sélectionner un receveur --</option>
                        <c:forEach var="receveur" items="${receveurs}">
                            <option value="${receveur.id}">
                                ${receveur.nomComplet} - ${receveur.groupeSanguin.designation} - 
                                Besoin: ${receveur.typeBesoin.libelle} - 
                                Urgence: ${receveur.urgence.libelle}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <button type="submit" class="btn btn-success">🔗 Attribuer</button>
                    <a href="${pageContext.request.contextPath}/don?action=view&id=${don.id}" 
                       class="btn btn-secondary">✖️ Annuler</a>
                </div>
            </form>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>