package com.donsang.servlet;

import com.donsang.model.Receveur;
import com.donsang.model.Receveur.*;
import com.donsang.model.TypeDeSang;
import com.donsang.service.ReceveurService;
import com.donsang.util.ValidationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReceveurServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReceveurService receveurService;
    
    @Override
    public void init() throws ServletException {
        receveurService = ReceveurService.getInstance();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";
        
        try {
            switch (action) {
                case "list": listerReceveurs(request, response); break;
                case "new": afficherFormulaireAjout(request, response); break;
                case "edit": afficherFormulaireModification(request, response); break;
                case "delete": supprimerReceveur(request, response); break;
                case "view": afficherReceveur(request, response); break;
                case "search": rechercherReceveurs(request, response); break;
                default: listerReceveurs(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/pages/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if ("insert".equals(action)) {
                ajouterReceveur(request, response);
            } else if ("update".equals(action)) {
                modifierReceveur(request, response);
            } else if ("changeStatut".equals(action)) {
                changerStatut(request, response);
            }
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("receveur", construireReceveurDepuisRequete(request));
            request.getRequestDispatcher("/pages/receveur/form.jsp").forward(request, response);
        }
    }
    
    private void listerReceveurs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Receveur> receveurs = receveurService.getTousReceveurs();
        request.setAttribute("receveurs", receveurs);
        request.setAttribute("totalReceveurs", receveurService.compterReceveurs());
        request.setAttribute("receveursEnAttente", receveurService.compterReceveursEnAttente());
        request.setAttribute("receveursCritiques", receveurService.compterReceveursCritiques());
        request.getRequestDispatcher("/pages/receveur/list.jsp").forward(request, response);
    }
    
    private void afficherFormulaireAjout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("typesSanguins", TypeDeSang.values());
        request.setAttribute("typesBesoins", TypeBesoin.values());
        request.setAttribute("niveauxUrgence", UrgenceNiveau.values());
        request.setAttribute("statuts", StatutReceveur.values());
        request.getRequestDispatcher("/pages/receveur/form.jsp").forward(request, response);
    }
    
    private void afficherFormulaireModification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Optional<Receveur> receveur = receveurService.getReceveur(id);
        
        if (receveur.isPresent()) {
            request.setAttribute("receveur", receveur.get());
            request.setAttribute("typesSanguins", TypeDeSang.values());
            request.setAttribute("typesBesoins", TypeBesoin.values());
            request.setAttribute("niveauxUrgence", UrgenceNiveau.values());
            request.setAttribute("statuts", StatutReceveur.values());
            request.getRequestDispatcher("/pages/receveur/form.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Receveur introuvable");
            listerReceveurs(request, response);
        }
    }
    
    private void afficherReceveur(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Optional<Receveur> receveur = receveurService.getReceveur(id);
        
        if (receveur.isPresent()) {
            request.setAttribute("receveur", receveur.get());
            request.getRequestDispatcher("/pages/receveur/view.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Receveur introuvable");
            listerReceveurs(request, response);
        }
    }
    
    private void ajouterReceveur(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ValidationException {
        Receveur receveur = construireReceveurDepuisRequete(request);
        receveurService.creerReceveur(receveur);
        response.sendRedirect(request.getContextPath() + "/receveur?action=list&success=create");
    }
    
    private void modifierReceveur(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ValidationException {
        Receveur receveur = construireReceveurDepuisRequete(request);
        Long id = Long.parseLong(request.getParameter("id"));
        receveur.setId(id);
        receveurService.modifierReceveur(receveur);
        response.sendRedirect(request.getContextPath() + "/receveur?action=list&success=update");
    }
    
    private void supprimerReceveur(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ValidationException {
        Long id = Long.parseLong(request.getParameter("id"));
        receveurService.supprimerReceveur(id);
        response.sendRedirect(request.getContextPath() + "/receveur?action=list&success=delete");
    }
    
    private void changerStatut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ValidationException {
        Long id = Long.parseLong(request.getParameter("id"));
        StatutReceveur statut = StatutReceveur.valueOf(request.getParameter("statut"));
        receveurService.changerStatut(id, statut);
        response.sendRedirect(request.getContextPath() + "/receveur?action=view&id=" + id);
    }
    
    private void rechercherReceveurs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String typeRecherche = request.getParameter("searchType");
        String valeurRecherche = request.getParameter("searchValue");
        
        List<Receveur> receveurs;
        
        try {
            if ("nom".equals(typeRecherche)) {
                receveurs = receveurService.rechercherParNom(valeurRecherche);
            } else if ("groupeSanguin".equals(typeRecherche)) {
                TypeDeSang groupeSanguin = TypeDeSang.fromDesignation(valeurRecherche);
                receveurs = receveurService.rechercherParGroupeSanguin(groupeSanguin);
            } else {
                receveurs = receveurService.getTousReceveurs();
            }
            
            request.setAttribute("receveurs", receveurs);
            request.setAttribute("searchPerformed", true);
            request.getRequestDispatcher("/pages/receveur/list.jsp").forward(request, response);
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            listerReceveurs(request, response);
        }
    }
    
    private Receveur construireReceveurDepuisRequete(HttpServletRequest request) {
        Receveur receveur = new Receveur();
        
        receveur.setNom(request.getParameter("nom"));
        receveur.setPrenom(request.getParameter("prenom"));
        receveur.setEmail(request.getParameter("email"));
        receveur.setTelephone(request.getParameter("telephone"));
        
        String groupeSanguinStr = request.getParameter("groupeSanguin");
        if (groupeSanguinStr != null && !groupeSanguinStr.isEmpty()) {
            receveur.setGroupeSanguin(TypeDeSang.fromDesignation(groupeSanguinStr));
        }
        
        String dateNaissanceStr = request.getParameter("dateNaissance");
        if (dateNaissanceStr != null && !dateNaissanceStr.isEmpty()) {
            receveur.setDateNaissance(LocalDate.parse(dateNaissanceStr));
        }
        
        String typeBesoinStr = request.getParameter("typeBesoin");
        if (typeBesoinStr != null && !typeBesoinStr.isEmpty()) {
            receveur.setTypeBesoin(TypeBesoin.valueOf(typeBesoinStr));
        }
        
        String urgenceStr = request.getParameter("urgence");
        if (urgenceStr != null && !urgenceStr.isEmpty()) {
            receveur.setUrgence(UrgenceNiveau.valueOf(urgenceStr));
        }
        
        String statutStr = request.getParameter("statut");
        if (statutStr != null && !statutStr.isEmpty()) {
            receveur.setStatut(StatutReceveur.valueOf(statutStr));
        }
        
        receveur.setAdresse(request.getParameter("adresse"));
        receveur.setVille(request.getParameter("ville"));
        receveur.setCodePostal(request.getParameter("codePostal"));
        receveur.setOrganeNecessaire(request.getParameter("organeNecessaire"));
        receveur.setDetailsMedicaux(request.getParameter("detailsMedicaux"));
        receveur.setObservations(request.getParameter("observations"));
        
        return receveur;
    }
}