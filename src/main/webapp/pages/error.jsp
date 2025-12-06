<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header class="header">
            <h1>❌ Une Erreur s'est Produite</h1>
        </header>

        <div class="alert alert-danger">
            <h2>Message d'erreur:</h2>
            <p>
                <c:choose>
                    <c:when test="${not empty errorMessage}">
                        ${errorMessage}
                    </c:when>
                    <c:otherwise>
                        Une erreur inattendue s'est produite.
                    </c:otherwise>
                </c:choose>
            </p>
        </div>

        <div class="form-container">
            <p>Nous sommes désolés, mais une erreur s'est produite lors du traitement de votre demande.</p>

            <c:if test="${pageContext.errorData != null}">
                <div class="info-section">
                    <h3>Détails Techniques</h3>
                    <p><strong>Code d'erreur:</strong> ${pageContext.errorData.statusCode}</p>
                    <p><strong>URI demandée:</strong> ${pageContext.errorData.requestURI}</p>
                </div>
            </c:if>

            <div class="action-bar">
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                    🏠 Retour à l'accueil
                </a>
                <a href="javascript:history.back()" class="btn btn-secondary">
                    ← Page précédente
                </a>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>